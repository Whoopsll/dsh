<!-- <template>
  <div>
    <div class="font-bold p-2 m-4 text-4xl text-gray-500"> 短视频 </div>
    <iframe src="http://demo.dianhuiyun.com.cn:8000/video" frameborder="0" width="100%" scrolling="auto" style="height: 883px"></iframe>
  </div>
</template> -->

<template>
  <div class="table-box">
    <ProTable
      rowKey="id"
      ref="proTable"
      title="审核服务列表"
      :columns="columns"
      :requestApi="getTableList"
      :initParam="initParam"
      :dataCallback="dataCallback"
      :searchCol="{ xs: 1, sm: 2, md: 3, lg: 4, xl: 4 }"
    >
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')" v-hasPermi="['sys:audit:add']">新增审核服务</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="Crop" @click="openDrawer('查看', scope.row)">查看</el-button>
        <el-button type="primary" link :icon="Edit" v-show="scope.row.executeStatus != 2" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="primary" link :icon="Crop" v-show="scope.row.executeStatus == 2" @click="downloadReport(scope.row)">下载报告</el-button>
        <el-button type="primary" link :icon="Crop" v-show="scope.row.executeStatus == 2" @click="openReportDrawer('报告详情', scope.row)">报告详情</el-button>
        <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
      </template>
    </ProTable>

    <AddDialog ref="dialogRef" />
    <reportDialog ref="reportRef" />
  </div>
</template>

<script setup lang="tsx" name="AuditManage">
import { ref, reactive, computed, onUnmounted, onMounted } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { downloadAuditPdf } from '@/hooks/downloadFile'
import reportDialog from './components/ReportDialog.vue'
import { Edit, CirclePlus, Delete, Crop } from '@element-plus/icons-vue'
import { AuditApi } from '@/api/modules/contentAudit'
import AddDialog from './components/AuditAddDialog.vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useHandleData } from '@/hooks/useHandleData'
import { executeType } from '@/configs/enum'
import { useAppStoreWithOut } from '@/store/modules/app'
const appStore = useAppStoreWithOut()
const userInfo = computed(() => appStore.userInfo)
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

// 默认不做操作就直接在 ProTable 组件上绑定	:requestApi="getUserList"
const getTableList = (params: any) => {
  params.type = 0
  return AuditApi.page(params)
}

const downloadReport = (row: any) => {
  const id = row.id
  console.log(id)
  ElMessageBox.confirm('确认下载资源报告吗?', '温馨提示', { type: 'warning' }).then(() => downloadAuditPdf(row.id, row.name))
}

const deleteData = async (id: number) => {
  await useHandleData(AuditApi.remove, [id], `删除`)
  proTable.value.getTableList()
}

const dialogRef = ref()
//新增审核项
const openDrawer = (title: string, row = {}) => {
  row.type = 0
  let params = {
    title,
    row: { ...row },
    isView: title === '查看',
    api: title === '编辑' ? AuditApi.add : AuditApi.add,
    getTableList: proTable.value.getTableList,
    marginTop: '10vh',
    maxHeight: '400px'
  }
  dialogRef.value.acceptParams(params)
}

const reportRef = ref()
//报告弹框
const openReportDrawer = (title: string, row = {}) => {
  row.type = 2
  let params = {
    title,
    row: { ...row },
    isView: true,
    api: title === '编辑' ? AuditApi.add : AuditApi.add,
    getTableList: proTable.value.getTableList,
    marginTop: '10vh',
    maxHeight: '500px'
  }
  reportRef.value.acceptParams(params)
}

onMounted(() => {
  init()
})

const wsRef = ref()
const init = () => {
  const ws = new WebSocket(import.meta.env.VITE_WEBSOCKET_URL + userInfo.value.pkId)
  ws.onmessage = function (event) {
    let data = JSON.parse(event.data)
    console.log('websocket')
    console.log(data)
    if (data.userId === userInfo.value.pkId) {
      if (data.status === 2) {
        proTable.value.getTableList()
        ElMessage({
          message: '计划执行完成',
          type: 'success',
          plain: true
        })
      }
      if (data.status === 1) {
        proTable.value.getTableList()
      }
    }
  }
  wsRef.value = ws
}

onUnmounted(() => {
  wsRef.value.close()
})

const getExecuteResult = (result: any) => {
  if (result == -1) return ''
  if (result == 0) return '异常'
  if (result == 1) return '成功'
}

//获取测试执行状态
const getExecuteStatus = (result: any) => {
  if (result == 2) return '已完成'
  if (result == 0) return '未开始'
  if (result == 1) return '执行中'
}

const getExecuteStatusTag = (result: any) => {
  if (result == 2) return 'danger'
  if (result == 0) return 'info'
  if (result == 1) return 'success'
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    label: '序号',
    type: 'index',
    width: 80
  },
  {
    prop: 'name',
    label: '名称',
    width: 240,
    search: { el: 'input' }
  },
  {
    prop: 'fileName',
    label: '审核文件',
    width: 180
  },
  {
    prop: 'duration',
    label: '视频时长',
    width: 120
  },
  {
    prop: 'fileSize',
    label: '文件大小(KB)',
    width: 120
  },
  {
    prop: 'auditStartTime',
    label: '审核开始时间',
    width: 180
  },
  {
    prop: 'executeType',
    label: '执行类型',
    width: 120,
    enum: executeType,
    fieldNames: { label: 'label', value: 'value' }
  },
  {
    prop: 'executeStatus',
    label: '审核状态',
    width: 120,
    render: (scope) => {
      return <el-tag type={getExecuteStatusTag(scope.row.executeStatus)}>{getExecuteStatus(scope.row.executeStatus)}</el-tag>
    }
  },
  {
    prop: 'executeResult',
    label: '审核结果',
    width: 100,
    render: (scope) => {
      return scope.row.executeResult == -1 ? '' : <el-tag type={scope.row.executeResult === 0 ? 'danger' : 'success'}>{getExecuteResult(scope.row.executeResult)}</el-tag>
    }
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 180
  },
  { prop: 'operation', label: '操作', fixed: 'right' }
]
</script>
