<template>
  <div class="table-box">
    <ProTable rowKey="id" ref="proTable" title="出版社列表" :columns="columns" :requestApi="getTableList" :initParam="initParam" :dataCallback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')" v-hasPermi="['sys:category:add']">新增出版社</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button v-if="checkEdit(scope.row.id)" type="primary" link :icon="Edit" @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
        <el-button type="primary" link :icon="List" @click="openPublisherWxConfigDialog(scope.row)">公众号配置</el-button>
      </template>
    </ProTable>
    <PublisherDialog ref="dialogRef" />
    <PublisherWxConfigDialog ref="publisherWxConfigDialog" />
  </div>
</template>

<script setup lang="tsx" name="Book">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import PublisherDialog from './components/PublisherDialog.vue'
import { Edit, CirclePlus, List, Delete } from '@element-plus/icons-vue'
import PublisherWxConfigDialog from './components/PublisherWxConfigDialog.vue'
import { PublisherApi } from '@/api/modules/publisher'
import { useAppStoreWithOut } from '@/store/modules/app'
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
  return PublisherApi.page(params)
}

// 表格配置项
const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'name',
    label: '出版社'
  },
  { prop: 'description', label: '描述' },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 240
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 300 }
]

const appStore = useAppStoreWithOut()
const checkEdit = (pkId: number) => {
  return appStore.userInfo.roleId === 1 || (appStore.userInfo.roleId === 2 && appStore.publisherIdList.includes(pkId))
}

const publisherWxConfigDialog = ref()
const openPublisherWxConfigDialog = (row = {}) => {
  // 合并对应属性
  let params = {
    title: row.bookName,
    row: { ...row },
    maxHeight: '400px',
    publisherId: row.id
  }
  // 打开弹框
  publisherWxConfigDialog.value.acceptParams(params)
}

// 打开 drawer(新增、查看、编辑)
const dialogRef = ref()
const openDrawer = (title: string, row = {}) => {
  let params = {
    title,
    row: { ...row },
    isView: title === '查看',
    api: title === '编辑' ? PublisherApi.edit : PublisherApi.add,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  dialogRef.value.acceptParams(params)
}

const deleteData = async (id: number) => {
  await useHandleData(PublisherApi.remove, [id], `删除出版社`)
  proTable.value.getTableList()
}
</script>
