<template>
  <div class="table-box">
    <ProTable rowKey="id" ref="proTable" title="用户列表" :columns="columns" :requestApi="getTableList" :initParam="initParam" :dataCallback="dataCallback">
      <template #operation="scope">
        <el-button type="danger" link :icon="Delete" @click="deleteAccount(scope.row.id)">删除</el-button>
      </template>
    </ProTable>
  </div>
</template>

<script setup lang="tsx" name="UserManager">
import { ref, reactive } from 'vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { Delete } from '@element-plus/icons-vue'
import { UserApi } from '@/api/modules/user'
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
  return UserApi.page(params)
}

const deleteAccount = async (id: number) => {
  await useHandleData(UserApi.remove, [id], `删除用户`)
  proTable.value.getTableList()
}

// 表格配置项
const columns: ColumnProps<UserType>[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'avatar',
    label: '头像',
    width: 140,
    render: (scope) => {
      return (
        <div class={['flex', 'justify-center', 'p-1']}>
          <el-avatar shape={'square'} size={80} src={scope.row.avatar} />
        </div>
      )
    }
  },
  {
    prop: 'wxName',
    label: '用户名',
    width: 250
  },
  { prop: 'wxId', label: 'openId', width: 300 },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 250
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 330 }
]
</script>
