<template>
  <div class="table-box">
    <ProTable
      rowKey="id"
      ref="proTable"
      title="资源码列表"
      :columns="columns"
      :requestApi="getTableList"
      :initParam="initParam"
      :dataCallback="dataCallback"
      :searchCol="{ xs: 1, sm: 2, md: 3, lg: 4, xl: 4 }"
    >
      <template #tableHeader>
        <div style="display: flex">
          <span style="margin-top: 3px; margin-right: 10px">自动审核服务 </span>
          <el-button type="primary" @click="startAudit()" :disabled="getBtnDisabled(0)" :icon="Stamp">启动</el-button>
          <el-button type="danger" @click="stopAudit()" :disabled="getBtnDisabled(1)" :icon="VideoPause">停止</el-button>
          <div style="margin-left: 20px; margin-top: 5px; height: 20px; display: flex; align-items: center">
            状态: <el-avatar shape="circle" :size="15" :src="getRunIcon()" style="margin-left: 5px; margin-right: 3px" />
            <span :style="{ color: getStatusFontColor() }">{{ getStatusFontText() }}</span>
            <span style="margin-left: 30px; margin-right: 5px">执行方式：</span>
            <span style="color: #1baff6; max-width: 300px">{{ auditTaskInfo.executionTime == '' ? '立即执行' : taskIntervalWeekStr }}</span>
          </div>
          <el-button style="position: absolute; right: 100px" type="primary" icon="Setting" @click="openDrawer()">配置</el-button>
          <AddDialog ref="dialogRef" />
        </div>
      </template>
    </ProTable>
  </div>
</template>

<script setup lang="tsx">
import { onMounted, onUnmounted, ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { AuditApi } from '@/api/modules/audit'
import { BookResourceApi } from '@/api/modules/book'
import { useAuthButtons } from '@/hooks/useAuthButtons'
import AddDialog from './components/AddDialog.vue'
import { Stamp, VideoPause } from '@element-plus/icons-vue'
import iconRun from '@/assets/images/icon_run.png'
import iconStop from '@/assets/images/icon_stop.png'
const { BUTTONS } = useAuthButtons()
// 获取 ProTable 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const proTable = ref()
// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({})

// dataCallback 是对于返回的表格数据做处理，如果你后台返回的数据不是 list && total 这些字段，那么你可以在这里进行处理成这些字段
const dataCallback = (data: any) => {
  return {
    list: data.list,
    total: data.total
  }
}

const getStatusFontColor = () => {
  if (auditTaskInfo.value.executionTime === '') {
    return auditStatus.value ? '#00da67' : '#bcbfbf'
  } else {
    return auditTaskInfo.value.status == 0 ? '#bcbfbf' : auditTaskInfo.value.status == 1 ? '#ff5f36' : '#00da67'
  }
}
const getStatusFontText = () => {
  if (auditTaskInfo.value.executionTime === '') {
    return auditStatus.value ? '审核中' : '未启动'
  } else {
    return auditTaskInfo.value.status == 0 ? '未启动' : auditTaskInfo.value.status == 1 ? '未开始' : '审核中'
  }
}

//获取运行图标
const getRunIcon = () => {
  if (auditTaskInfo.value.executionTime === '') {
    return auditStatus.value ? iconRun : iconStop
  } else {
    return auditTaskInfo.value.status == 0 ? iconStop : auditTaskInfo.value.status == 1 ? iconStop : iconRun
  }
}

onMounted(() => {
  startTimer()
  proTable.value.getTableList()
})

onUnmounted(() => {
  stopTimer()
})

// 审核状态
const auditStatus = ref(false)

const getBtnDisabled = (index: number) => {
  if (index == 0) {
    // 开启按钮
    if (auditTaskInfo.value.executionTime === '') {
      return auditStatus.value
    } else {
      return auditTaskInfo.value.status != 0
    }
  } else {
    //停止按钮
    if (auditTaskInfo.value.executionTime === '') {
      return !auditStatus.value
    } else {
      return auditTaskInfo.value.status == 0
    }
  }
}

const startAudit = () => {
  auditStatus.value = true
  var param = null
  if (auditTaskInfo.value.executionTime == '') {
    param = {
      interval: 0
    }
  } else {
    var times = auditTaskInfo.value.executionTime.split(',')
    param = {
      interval: 1,
      cron: auditTaskInfo.value.executionTime.slice(0, 13),
      time: times[times.length - 2] + ':' + times[times.length - 1]
    }
  }
  AuditApi.startAuditTask(param)
    .then((res) => {
      if (res.code == 0) {
        auditStatus.value = false
        proTable.value.getTableList()
      }
    })
    .catch((err) => {
      console.log(err)
      auditStatus.value = false
    })
}

const stopAudit = () => {
  AuditApi.stopAuditTask().then((res) => {
    if (res.code == 0) {
      auditStatus.value = false
      proTable.value.getTableList()
    }
  })
}

const taskParams = ref({})
const auditTaskInfo = ref({})
const taskIntervalWeekStr = ref('')
const taskIntervalWeekConstant = ref(['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'])
// 默认不做操作就直接在 ProTable 组件上绑定	:requestApi="getUserList"
const getTableList = (params: any) => {
  getAuditTask()
  return BookResourceApi.auditResourcePage(params)
}

let timer: ReturnType<typeof setInterval> | null = null

// 定时器逻辑
const startTimer = () => {
  timer = setInterval(() => {
    getAuditTask()
  }, 2000) // 每秒更新一次时间
}

// 清理定时器
const stopTimer = () => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

const getAuditTask = () => {
  AuditApi.getAuditTask().then((res) => {
    if (res.code == 0) {
      auditTaskInfo.value = res.data
      if (res.data.executionTime == '') {
        // 执行周期为立刻执行
        taskParams.value = {
          interval: 0,
          date: [],
          time: '',
          email: auditTaskInfo.value.email
        }
      } else {
        //解析时间
        var time = res.data.executionTime.split(',')
        taskIntervalWeekStr.value = ''
        console.log('time==')
        console.log(time)
        var dates = []
        var timeStr = null
        for (var i = 0; i < time.length; i++) {
          if (i == time.length - 2) {
            // timeStr = new Date(2024, 1, 1, time[i], time[i + 1])
            timeStr = time[i] + ':' + time[i + 1]
          } else if (i != time.length - 1) {
            if (time[i] == 1) {
              //当前日期被选中
              dates[dates.length] = i + 1 + ''
              taskIntervalWeekStr.value += taskIntervalWeekConstant.value[i] + ','
            }
          }
        }
        taskIntervalWeekStr.value = taskIntervalWeekStr.value.slice(0, taskIntervalWeekStr.value.length - 1) + '  ' + timeStr
        taskParams.value = {
          interval: 1,
          date: dates,
          time: timeStr,
          email: auditTaskInfo.value.email
        }
      }
    }
  })
}

const dialogRef = ref()
//新增审核项
const openDrawer = () => {
  taskParams.value.taskInfo = auditTaskInfo.value
  let params = {
    title: '配置审核项',
    api: AuditApi.edit,
    row: taskParams.value,
    getTableList: proTable.value.getTableList,
    maxHeight: '300px'
  }
  console.log(params)
  dialogRef.value.acceptParams(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'bookCover',
    label: '封面',
    width: 150,
    render: (scope) => {
      return (
        <div class={['flex', 'justify-center', 'p-1']}>
          <el-avatar shape={'square'} size={100} src={scope.row.bookCover} />
        </div>
      )
    }
  },
  {
    prop: 'bookName',
    label: '书名',
    width: 300
  },
  {
    prop: 'resourceName',
    label: '资源名',
    width: 200
  },
  {
    prop: 'resourceName',
    label: '资源名',
    width: 200
  },
  {
    prop: 'link',
    label: '资源链接',
    width: 400,
    render: (scope) => {
      return (
        <a style="color: #08a3fa" href={scope.row.link} target="_blank">
          {scope.row.link}
        </a>
      )
    }
  },
  {
    prop: 'result',
    label: '审核结果',
    width: 150,
    render: (scope) => {
      return <el-tag type={scope.row.result == 1 ? 'danger' : 'success'}>{scope.row.result == 1 ? '异常' : '正常'}</el-tag>
    }
  },
  {
    prop: 'isChanged',
    label: '是否阻断访问',
    width: 200,
    render: (scope) => {
      return scope.row.type == 0 ? (
        <el-tag type={scope.row.isCanAccess == 0 ? 'danger' : 'success'}>{scope.row.isCanAccess == 0 ? '是' : '否'}</el-tag>
      ) : (
        <el-tag type="danger">第三方资源，无法阻断</el-tag>
      )
    }
  },
  {
    prop: 'isChanged',
    label: '是否失效',
    width: 150,
    render: (scope) => {
      return (
        <>
          {BUTTONS.value.status ? (
            <el-switch model-value={scope.row.status} active-text={scope.row.isChanged ? '是' : '否'} active-value={1} inactive-value={0} />
          ) : (
            <el-tag type={scope.row.isChanged ? 'danger' : 'success'}>{scope.row.isChanged ? '是' : '否'}</el-tag>
          )}
        </>
      )
    }
  },
  {
    prop: 'updateTime',
    label: '审核时间',
    width: 200
  }
]
</script>

<style scoped></style>
