<template>
  <div class="table-box">
    <ProTable rowKey="id" ref="proTable" title="帖子管理" :columns="columns" :requestApi="getTableList" :initParam="initParam" :dataCallback="dataCallback">
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link @click="showDetail(scope.row)">查看</el-button>
      </template>
    </ProTable>
    <comment-dialog ref="detailDialogRef" />
  </div>
</template>

<script setup lang="tsx" name="Book">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { MixedApi } from '@/api/modules/mixed'
import CommentDialog from './components/CommentDialog.vue'

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
  return MixedApi.page(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'cover',
    label: '封面',
    width: 150,
    render: (scope) => {
      return (
        <div class={['flex', 'justify-center', 'p-1']}>
          <el-avatar shape={'square'} size={100} src={scope.row.cover} />
        </div>
      )
    }
  },
  {
    prop: 'name',
    label: '论坛名',
    width: 300,
    search: { el: 'input', key: 'forumName' }
  },
  { prop: 'bookName', label: '书名', width: 300 },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 200
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 240 }
]

const detailDialogRef = ref()
const showDetail = (row = {}) => {
  let params = {
    row: { ...row }
  }
  detailDialogRef.value.acceptParams(params)
}
</script>

<style scoped lang="less">
:deep(audio) {
  width: 100% !important;
}
</style>
