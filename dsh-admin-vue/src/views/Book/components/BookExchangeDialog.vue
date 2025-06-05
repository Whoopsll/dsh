<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" width="80%" :cancel-dialog="cancelDialog">
    <div class="el-dialog__body">
      <ProTable rowKey="id" ref="proTable" title="资源兑换列表" :columns="columns" :requestApi="getTableList" :dataCallback="dataCallback">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button type="primary" :icon="Download" @click="downloadFileTemplate">下载上传模板</el-button>
          <el-button type="primary" :icon="Download" @click="downloadFile">下载兑换码zip</el-button>
          <el-button type="primary" :icon="Download" @click="downloadFileXlsx">下载兑换码xlsx</el-button>
          <ImportResource v-model:code="importRes" :file-size="1" :api="BookApi.importExchange" @check-validate="afterUpload" :bookId="dialogProps.row.id">
            <template #empty>
              <el-button type="primary" :icon="UploadFilled">导入兑换码</el-button>
            </template>
          </ImportResource>
        </template>
        <!-- 表格操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="Grid" @click="showQrCode(scope.row)">二维码</el-button>
          <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
        </template>
      </ProTable>
    </div>
  </Dialog>
  <Dialog :model-value="showQrCodeFlag" :maxHeight="300" :width="500" title="兑换二维码" :cancel-dialog="cancelResourceDialog">
    <div class="code-dialog"><el-image :src="qrcode" style="height: 300px; width: 300px"></el-image></div>

    <template #footer>
      <slot name="footer">
        <el-button type="primary" v-loading.fullscreen.lock="fullscreenLoading" @click="downloadQrCode">下载</el-button>
      </slot>
    </template>
  </Dialog>
</template>

<script setup lang="tsx">
import { Dialog } from '@/components/Dialog'
import { ref } from 'vue'
import { BookApi } from '@/api/modules/book'
import { ColumnProps } from '@/components/ProTable/interface'
import { Grid, UploadFilled, Delete, Download } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import ProTable from '@/components/ProTable/index.vue'
import { useQRCode } from '@vueuse/integrations/useQRCode'
import { useHandleData } from '@/hooks/useHandleData'
import ImportResource from '@/components/Upload/ImportResource.vue'
import { useDownload } from '@/hooks/useDownload'

// 定义弹出框类型
interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
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
  params.bookId = dialogProps.value.row.id
  return BookApi.exchangePage(params)
}
const columns: ColumnProps[] = [
  {
    prop: 'id',
    label: '编号',
    width: 100
  },
  {
    prop: 'verifyCode',
    label: '校验码',
    width: 200
  },
  {
    prop: 'bookLink',
    label: '内容链接',
    width: 500
  },
  {
    prop: 'status',
    label: '状态',
    width: 150,
    render: (scope) => {
      return <el-tag type={scope.row.status === 1 ? 'danger' : 'success'}>{scope.row.status === 1 ? '已使用' : '未使用'}</el-tag>
    }
  },
  {
    prop: 'userFlag',
    label: '兑换人',
    width: 300
  },
  {
    prop: 'exchangeTime',
    label: '兑换时间',
    width: 300
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 200 }
]

let qrcode
const showQrCodeFlag = ref(false)
const showQrCode = (row) => {
  qrcode = useQRCode(row.qrCodeLink)
  showQrCodeFlag.value = true
}
const fullscreenLoading = ref(false)
const downloadQrCode = () => {
  fullscreenLoading.value = true
  setTimeout(() => {
    fullscreenLoading.value = false
  }, 1500)
  let a = document.createElement('a')
  a.href = qrcode.value
  a.setAttribute('download', '二维码')
  a.click()
}

const cancelResourceDialog = () => {
  showQrCodeFlag.value = false
}

const importRes = ref()
const afterUpload = () => {
  if (importRes.value == 0) {
    proTable.value.getTableList()
  }
}

const deleteData = async (id: number) => {
  await useHandleData(BookApi.removeExchange, [id], `删除书籍兑换`)
  proTable.value.getTableList()
}

const downloadFile = () => {
  ElMessageBox.confirm('确认下载全部兑换码?', '温馨提示', { type: 'warning' }).then(() =>
    useDownload(BookApi.downloadExchange, '兑换二维码', dialogProps.value.row.id, true, '.zip')
  )
}

const downloadFileXlsx = () => {
  ElMessageBox.confirm('确认下载兑换码表格吗?', '温馨提示', { type: 'warning' }).then(() =>
    useDownload(BookApi.downloadExchangeXlsx, '兑换二维码', dialogProps.value.row.id, true, '.xlsx')
  )
}

const downloadFileTemplate = () => {
  window.location.href = 'http://demo.dianhuiyun.com.cn:9000/dsh/%E4%B9%A6%E9%93%BE%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx'
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
