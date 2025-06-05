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
        <el-button type="primary" link :icon="Crop" @click="openDrawer('查看', scope.row)" v-hasPermi="['sys:audit:view']">查看</el-button>
        <el-button type="primary" link :icon="Edit" @click="openDrawer('编辑', scope.row)" v-hasPermi="['sys:audit:edit']">编辑</el-button>
        <el-button type="primary" link :icon="Crop" @click="downloadReport(scope.row)" v-hasPermi="['sys:audit:downloadReport']">下载报告</el-button>
        <el-button type="primary" link :icon="Open" @click="modifyStatus(scope.row)" :disabled="scope.row.status === 1" v-hasPermi="['sys:audit:edit']">运行</el-button>
        <el-button type="primary" link :icon="TurnOff" @click="modifyStatus(scope.row)" :disabled="scope.row.status === 0" v-hasPermi="['sys:audit:edit']">停止</el-button>
        <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)" v-hasPermi="['sys:audit:delete']">删除</el-button>
      </template>
    </ProTable>

    <AddDialog ref="dialogRef" />
  </div>
</template>

<script setup lang="tsx" name="AuditManage">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import iconRun from '@/assets/images/icon_run.png'
import iconStop from '@/assets/images/icon_stop.png'
import { downloadPdf } from '@/hooks/downloadFile'
import { Edit, CirclePlus, TurnOff, Open, Delete, Crop } from '@element-plus/icons-vue'
import { AuditApi } from '@/api/modules/audit'
import AddDialog from './components/WebAuditAddDialog.vue'
import { ElMessageBox } from 'element-plus'
import { useHandleData } from '@/hooks/useHandleData'

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
  return AuditApi.page(params)
}

const downloadReport = (row: any) => {
  const id = row.id
  console.log(id)
  ElMessageBox.confirm('确认下载资源报告吗?', '温馨提示', { type: 'warning' }).then(() => downloadPdf())
}

const deleteData = async (id: number) => {
  await useHandleData(AuditApi.remove, [id], `删除`)
  proTable.value.getTableList()
}

const modifyStatus = async (params: any) => {
  var str = ''
  var param = JSON.parse(JSON.stringify(params))
  if (param.status === 1) {
    // 停止服务
    param.status = 0
    str = '停止服务'
  } else {
    // 启动服务
    param.status = 1
    str = '启动服务'
  }
  await useHandleData(AuditApi.edit, param, str)
  proTable.value.getTableList()
}

const dialogRef = ref()
//新增审核项
const openDrawer = (title: string, row = {}) => {
  let params = {
    title,
    row: { ...row },
    isView: title === '查看',
    api: title === '编辑' ? AuditApi.edit : AuditApi.add,
    getTableList: proTable.value.getTableList,
    marginTop: '10vh',
    maxHeight: '300px'
  }
  dialogRef.value.acceptParams(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'id',
    label: '序号',
    width: 80
  },
  {
    prop: 'name',
    label: '审核服务名称',
    width: 240,
    search: { el: 'input' }
  },
  {
    prop: 'name',
    label: '审核服务类型',
    width: 240
  },
  {
    prop: 'serviceAddress',
    label: '服务地址',
    width: 180
  },
  {
    prop: 'status',
    label: '运行状态',
    width: 120,
    render: (scope) => {
      return scope.row.status === 1 ? (
        <div style="display:flex; align-items:center; justify-content: center;width: 100%">
          <el-avatar shape={'circle'} size={15} src={iconRun} />
          <div style="margin-left: 5px;">运行中</div>
        </div>
      ) : (
        <div style="display:flex; align-items:center; justify-content: center;width: 100%">
          <el-avatar shape={'circle'} size={15} src={iconStop} />
          <div style="margin-left: 5px;">停止</div>
        </div>
      )
    }
  },
  {
    prop: 'account',
    label: '账号',
    width: 160
  },
  {
    prop: 'password',
    label: '密码',
    width: 140
  },
  {
    prop: '',
    label: '审核结果',
    width: 100
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 200
  },
  { prop: 'operation', label: '操作', fixed: 'right' }
]
</script>
