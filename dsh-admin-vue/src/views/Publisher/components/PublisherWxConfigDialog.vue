<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" width="80%" :cancel-dialog="cancelDialog">
    <div class="el-dialog__body">
      <ProTable rowKey="id" ref="proTable" title="公众号配置信息" :columns="columns" :requestApi="getTableList" :dataCallback="dataCallback">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增公众号配置')">新增资源</el-button>
        </template>
        <!-- 表格操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="Edit" @click="openCustomMenuDialog('自定义菜单', scope.row)">自定义菜单</el-button>
          <el-button type="primary" link :icon="Edit" @click="openDrawer('编辑公众号配置', scope.row)">编辑</el-button>
          <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row)">删除</el-button>
        </template>
      </ProTable>
    </div>
  </Dialog>
  <PublisherWxConfigEditDialog :maxHeight="400" ref="dialogRef" />
  <CustomMenuDialog :maxHeight="500" ref="customMenuRef" />
</template>

<script setup lang="tsx">
import { Dialog } from '@/components/Dialog'
import { ref } from 'vue'
import { ConfigApi } from '@/api/modules/config'
import { ColumnProps } from '@/components/ProTable/interface'
import { Edit, Delete, CirclePlus } from '@element-plus/icons-vue'
import ProTable from '@/components/ProTable/index.vue'
import PublisherWxConfigEditDialog from './PublisherWxConfigEditDialog.vue'
import CustomMenuDialog from './CustomMenuDialog.vue'
import { useHandleData } from '@/hooks/useHandleData'

// 定义弹出框类型
interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  publisherId: number
  labelWidth?: number
  maxHeight?: number | string
  api?: (params: any) => Promise<any>
  getTableList?: () => Promise<any>
}
// 弹出框是否显示
const dialogVisible = ref(false)
// 弹出框属性
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: {},
  publisherId: 0,
  labelWidth: 160,
  fullscreen: false,
  maxHeight: 700
})

// 接收父组件传过来的参数
const acceptParams = async (params: DialogProps) => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
}

defineExpose({
  acceptParams
})

// 关闭弹框
const cancelDialog = () => {
  dialogVisible.value = false
}

// 获取 ProTable 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const proTable = ref()

// dataCallback 是对于返回的表格数据做处理，如果你后台返回的数据不是 list && total 这些字段，那么你可以在这里进行处理成这些字段
const dataCallback = (data: any) => {
  return {
    list: data.list,
    total: data.total
  }
}

const getTableList = (params: any) => {
  params.publisherId = dialogProps.value.publisherId
  return ConfigApi.page(params)
}

const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'name',
    label: '配置名称',
    width: 200
  },
  {
    prop: 'account',
    label: '公众号Key',
    width: 400
  },
  {
    prop: 'password',
    label: '公众号Secret',
    width: 400
  },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 200
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 160 }
]

const dialogRef = ref()
const openDrawer = (title: string, row = {}) => {
  if (row.resourceGetType == null) {
    row.resourceGetType = 0
  }
  let params = {
    title,
    row: { ...row },
    publisherId: dialogProps.value.publisherId,
    api: ConfigApi.edit,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  dialogRef.value.acceptParams(params)
}

const customMenuRef = ref()
const openCustomMenuDialog = (title: string, row = {}) => {
  row.name = ''
  row.content = ''
  let params = {
    title,
    row: { ...row },
    api: ConfigApi.edit,
    getTableList: proTable.value.getTableList
  }
  customMenuRef.value.acceptParams(params)
}

const deleteData = async (row: {}) => {
  await useHandleData(ConfigApi.remove, row, `删除公众号配置`)
  proTable.value.getTableList()
}
</script>

<style scoped>
.code-dialog {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
