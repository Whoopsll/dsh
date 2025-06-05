<template>
  <Dialog :model-value="dialogVisible" top="8vh" title="公告" :cancel-dialog="cancelDialog" width="75vw">
    <ProTable rowKey="id" ref="proTable" title="通知列表" :columns="columns" :requestApi="getTableList" :initParam="initParam" :dataCallback="dataCallback">
      <template #tableHeader="scope">
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增', { forumId: dialogProps.row.forumId })">新增</el-button>
        <el-button
          type="danger"
          :icon="Delete"
          plain
          :disabled="!scope.isSelected"
          @click="deleteBatch(scope.selectedListIds as unknown as number[])"
          v-hasPermi="['sys:feedback:remove']"
          >批量删除</el-button
        >
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="danger" link @click="deleteBatch([scope.row.id])" v-hasPermi="['sys:notice:remove']">删除</el-button>
      </template>
    </ProTable>
    <NoticeEditDialog ref="noticeEditDialogRef" />
  </Dialog>
</template>

<script setup lang="tsx" name="Notice">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { NoticeApi } from '@/api/modules/notice'
import { Dialog } from '@/components/Dialog'
import { CirclePlus, Delete } from '@element-plus/icons-vue'
import NoticeEditDialog from './NoticeEditDialog.vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const dialogVisible = ref(false)
const dialogProps = ref({
  row: {}
})

// 接收父组件传过来的参数
const acceptParams = (params): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  params.row.forumId = params.row.id
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
}

const cancelDialog = () => {
  dialogVisible.value = false
}

defineExpose({
  acceptParams
})

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
  params.forumId = dialogProps.value.row.forumId
  return NoticeApi.pageByForum(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'title',
    label: '标题'
  },
  { prop: 'content', label: '内容' },
  { prop: 'validTime', label: '生效时间' },
  { prop: 'adminName', label: '发布人' },
  {
    prop: 'isTop',
    label: '是否置顶',
    render: (scope) => {
      return (
        <div>
          <el-tag type={scope.row.isTop === 1 ? 'success' : 'info'}>{scope.row.isTop === 1 ? '置顶' : '默认'}</el-tag>
        </div>
      )
    }
  },
  {
    prop: 'createTime',
    label: '创建时间'
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 150 }
]

const noticeEditDialogRef = ref()
const openDrawer = (title: string, row = {}) => {
  let params = {
    title,
    row: {
      ...row,
      forumId: dialogProps.value.row.forumId
    },
    isView: title === '查看',
    api: title === '编辑' ? NoticeApi.edit : NoticeApi.add,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  noticeEditDialogRef.value.acceptParams(params)
}

// 批量删除
const deleteBatch = async (ids: number[]) => {
  console.log(ids)
  if (ids.length === 0) {
    ElMessageBox.alert('请选择需要删除的反馈', '提示', { type: 'warning' })
    return
  }
  ElMessageBox.confirm('是否确认删除选中的反馈？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await NoticeApi.remove(ids)
      ElMessage.success('删除成功')
      proTable.value?.clearSelection()
      proTable.value?.getTableList()
    } catch (error) {
      console.log(error)
      ElMessage.error('删除失败')
    }
  })
}
</script>
