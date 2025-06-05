<template>
  <div class="table-box">
    <ProTable
      :pagination="false"
      :indent="20"
      rowKey="id"
      ref="proTable"
      title="书籍分类列表"
      :columns="columns"
      :requestApi="getTableList"
      :initParam="initParam"
      :dataCallback="dataCallback"
    >
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')" v-hasPermi="['sys:category:add']">新增分类</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link :icon="Edit" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="danger" link v-show="scope.row.description.indexOf('中图法') == -1" :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
      </template>
    </ProTable>
    <CategoryDialog ref="dialogRef" />
  </div>
</template>

<script setup lang="tsx" name="Book">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import CategoryDialog from './components/CategoryDialog.vue'
import { SysType } from '@/api/interface'
import { Edit, CirclePlus, Delete } from '@element-plus/icons-vue'
import { CategoryApi } from '@/api/modules/category'
import { useHandleData } from '@/hooks/useHandleData'

// 获取 ProTable 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const proTable = ref()
// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({})

// dataCallback 是对于返回的表格数据做处理，如果你后台返回的数据不是 list && total 这些字段，那么你可以在这里进行处理成这些字段
// const dataCallback = (data: any) => {
//   return {
//     list: data.list,
//     total: data.total
//   }
// }

// 默认不做操作就直接在 ProTable 组件上绑定	:requestApi="getUserList"
const getTableList = (params: any) => {
  return CategoryApi.page(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'name',
    label: '分类名',
    align: 'left'
  },
  { prop: 'description', label: '分类描述' },
  {
    prop: 'createTime',
    label: '创建时间'
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 200 }
]

// 打开 drawer(新增、查看、编辑)
const dialogRef = ref()
const openDrawer = (title: string, row: Partial<SysType.ResTypeList> = {}) => {
  let params = {
    title,
    row: { ...row },
    isView: title === '查看',
    api: title === '编辑' ? CategoryApi.edit : CategoryApi.add,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  dialogRef.value.acceptParams(params)
}
const deleteData = async (id: number) => {
  await useHandleData(CategoryApi.remove, [id], `删除分类`)
  proTable.value.getTableList()
}
</script>
