<template>
  <div class="table-box">
    <ProTable
      rowKey="id"
      ref="proTable"
      title="意见反馈列表"
      :columns="columns"
      :requestApi="getTableList"
      :initParam="initParam"
      :dataCallback="dataCallback"
      :searchCol="{ xs: 1, sm: 2, md: 3, lg: 4, xl: 4 }"
    >
      <!-- 表格 header 按钮 -->
      <template #tableHeader="scope">
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
        <el-button type="danger" link :icon="Delete" @click="deleteRow(scope.row)" v-hasPermi="['sys:feedback:remove']">删除</el-button>
      </template>
    </ProTable>
  </div>
</template>

<script setup lang="tsx" name="Book">
import { ref, reactive } from 'vue'
import { Delete } from '@element-plus/icons-vue'
import { FeedbackApi } from '@/api/modules/feedback'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { ElMessageBox, ElMessage } from 'element-plus'
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
  return FeedbackApi.page(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  { prop: 'username', label: '反馈人', width: 150 },
  { prop: 'content', label: '反馈意见', width: 150 },
  {
    prop: 'img',
    label: '图片',
    width: 150,
    render: (scope) => {
      return (
        <div class={['flex', 'justify-center', 'p-1']}>
          <el-avatar shape={'square'} size={100} src={scope.row.img} />
        </div>
      )
    }
  },
  {
    prop: 'createTime',
    label: '反馈时间',
    width: 200
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 240 }
]

// 删除单个
const deleteRow = async (params) => {
  await useHandleData(FeedbackApi.delete, [params.id], `删除`)
  proTable.value.getTableList()
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
      await FeedbackApi.delete(ids)
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
