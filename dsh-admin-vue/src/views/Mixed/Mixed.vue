<template>
  <div class="table-box">
    <ProTable rowKey="id" ref="proTable" title="融合管理" :columns="columns" :requestApi="getTableList" :initParam="initParam" :dataCallback="dataCallback">
      <!-- 表格 header 按钮 -->
      <template #tableHeader>
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer('新增')" v-hasPermi="['sys:manager:add']">新建圈子</el-button>
      </template>
      <!-- 表格操作 -->
      <template #operation="scope">
        <el-button type="primary" link @click="showDetail(scope.row)">查看</el-button>
        <el-button type="primary" link @click="openDrawer('编辑', scope.row)">编辑</el-button>
        <el-button type="primary" link :icon="Checked" :style="{ color: scope.row.isTop == 1 ? 'red' : '#429488' }" @click="stickyForum(scope.row.id)">{{
          scope.row.isTop == 0 ? '置顶书籍' : '取消置顶'
        }}</el-button>
        <el-button type="primary" link @click="showNotice(scope.row)">公告</el-button>
        <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
      </template>
    </ProTable>
    <forum-edit-dialog ref="editDialogRef" />
    <forum-dialog ref="detailDialogRef" />
    <NoticeListDialog ref="noticeDialogRef" />
  </div>
</template>

<script setup lang="tsx" name="Book">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Checked, CirclePlus, Delete } from '@element-plus/icons-vue'
import { ColumnProps } from '@/components/ProTable/interface'
import ProTable from '@/components/ProTable/index.vue'
import { MixedApi } from '@/api/modules/mixed'
import forumEditDialog from './components/ForumEditDialog.vue'
import forumDialog from './components/ForumDialog.vue'
import NoticeListDialog from './components/NoticeListDialog.vue'
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
  console.log('请求接口')
  return MixedApi.page(params)
}

//置顶书籍
const stickyForum = (id: number) => {
  MixedApi.stickyForum(id).then((res) => {
    if (res.code == 0) {
      ElMessage({
        type: 'success',
        message: `操作成功!`
      })
      proTable.value.getTableList()
    }
  })
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
    prop: 'avatar',
    label: '头像',
    width: 150,
    render: (scope) => {
      return (
        <div class={['flex', 'justify-center', 'p-1']}>
          <el-avatar shape={'square'} size={100} src={scope.row.avatar} />
        </div>
      )
    }
  },
  {
    prop: 'name',
    label: '论坛名',
    width: 150,
    search: { el: 'input', key: 'forumName' }
  },
  { prop: 'username', label: '圈主', width: 150, search: { el: 'input' } },
  { prop: 'bookName', label: '书名', width: 150 },
  { prop: 'categoryName', label: '类别', width: 150 },
  {
    prop: 'voiceIntroduction',
    label: '语音介绍',
    width: 400,
    render: (scope) => {
      return <audio controls src={scope.row.voiceIntroduction} class={'audio-voice'}></audio>
    }
  },
  { prop: 'price', label: '作者答缺省价', width: 150 },
  { prop: 'writerIntroduction', label: '作者简介', width: 300 },
  { prop: 'userNumber', label: '参与人数', width: 100 },
  {
    prop: 'createTime',
    label: '创建时间',
    width: 200
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 200 }
]

// 打开 drawer(新增、查看、编辑)
const editDialogRef = ref()
const openDrawer = (title: string, row = {}) => {
  let params = {
    title,
    row: { ...row },
    isView: title === '查看',
    api: title === '编辑' ? MixedApi.edit : MixedApi.add,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  editDialogRef.value.acceptParams(params)
}

const detailDialogRef = ref()
const showDetail = (row = {}) => {
  let params = {
    row: { ...row }
  }
  detailDialogRef.value.acceptParams(params)
}

const noticeDialogRef = ref()
const showNotice = (row = {}) => {
  let params = {
    row: { ...row }
  }
  noticeDialogRef.value.acceptParams(params)
}
const deleteData = async (id: number) => {
  await useHandleData(MixedApi.remove, [id], `删除论坛`)
  proTable.value.getTableList()
}
</script>

<style scoped lang="less">
:deep(audio) {
  width: 100% !important;
}
</style>
