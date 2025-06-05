<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" width="80%" :cancel-dialog="cancelDialog">
    <div class="el-dialog__body">
      <ProTable rowKey="id" ref="proTable" title="书籍资源列表" :columns="columns" :requestApi="getTableList" :dataCallback="dataCallback">
        <!-- 表格 header 按钮 -->
        <template #tableHeader>
          <el-button
            type="primary"
            v-if="(dialogProps.type == 2 && tableDataSize == 0) || dialogProps.type != 2"
            :icon="CirclePlus"
            @click="openDrawer('新增')"
            v-hasPermi="['sys:book:add']"
            >新增资源</el-button
          >
          <el-button type="primary" v-if="dialogProps.type != 2" :icon="DocumentCopy" @click="uploadTemplate" v-hasPermi="['sys:book:add']">下载上传模板</el-button>
          <el-button type="primary" v-if="dialogProps.type == 0" :icon="Download" @click="downloadFile" v-hasPermi="['sys:book:view']">下载资源码</el-button>
          <ImportResource
            v-model:code="importRes"
            :type="dialogProps.type"
            :file-size="1"
            :api="BookResourceApi.importResource"
            @check-validate="afterUpload"
            :bookId="dialogProps.row.id"
            v-if="dialogProps.type != 2"
          >
            <template #empty>
              <el-button type="primary" :icon="UploadFilled">导入资源码</el-button>
            </template>
          </ImportResource>
        </template>
        <!-- 表格操作 -->
        <template #operation="scope">
          <el-button type="primary" link :icon="Edit" @click="enabledChange(scope.row)">{{ scope.row.enabled === 0 ? '失效' : '生效' }}</el-button>
          <el-button type="primary" v-if="dialogProps.type == 0" link :icon="Grid" @click="showQrCode(scope.row)">二维码</el-button>
          <el-button type="primary" link :icon="Edit" @click="openDrawer('编辑', scope.row)">编辑</el-button>
          <el-button type="danger" link :icon="Delete" @click="deleteData(scope.row.id)">删除</el-button>
        </template>
      </ProTable>
    </div>
  </Dialog>
  <Dialog :model-value="showQrCodeFlag" :maxHeight="300" :width="500" title="资源二维码" :cancel-dialog="cancelResourceDialog">
    <div class="code-dialog">
      <!-- <el-image :src="qrcode" style="height: 300px; width: 300px"></el-image> -->
      <vue-qr id="payQR" :text="qrcode" :size="300" colorLight="#ffffff" :logoSrc="qrCodeLogo" logoScale="0.2" :callback="getImgInfo"> </vue-qr>
    </div>
    <template #footer>
      <slot name="footer">
        <el-button type="primary" v-loading.fullscreen.lock="fullscreenLoading" @click="downloadQrCode">下载</el-button>
      </slot>
    </template>
  </Dialog>
  <BookResourceEditDialog :maxHeight="100" ref="dialogRef" />
</template>

<script setup lang="tsx">
import { Dialog } from '@/components/Dialog'
import { ref } from 'vue'
import vueQr from 'vue-qr/src/packages/vue-qr.vue'
import { BookResourceApi } from '@/api/modules/book'
import { ConfigApi } from '@/api/modules/config'
import { ColumnProps } from '@/components/ProTable/interface'
import { Edit, CirclePlus, Grid, Download, DocumentCopy, UploadFilled, Delete } from '@element-plus/icons-vue'
import ProTable from '@/components/ProTable/index.vue'
import BookResourceEditDialog from './BookResourceEditDialog.vue'
// import { useQRCode } from '@vueuse/integrations/useQRCode'
import { useDownload } from '@/hooks/useDownload'
import { ElMessageBox, ElMessage } from 'element-plus'
import ImportResource from '@/components/Upload/ImportResource.vue'
import { useHandleData } from '@/hooks/useHandleData'

// 定义弹出框类型
interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  type: number
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
  type: 0,
  labelWidth: 160,
  fullscreen: false,
  maxHeight: 700
})

// 接收父组件传过来的参数
const acceptParams = async (params: DialogProps) => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  if (params.row.wxConfigId == 0) {
    // 默认二维码，不需要获取公众号配置信息
    qrCodeLogo.value = ''
  } else {
    // 获取公众号配置信息
    getConfigById(params.row.wxConfigId)
  }
}

defineExpose({
  acceptParams
})

// 关闭弹框
const cancelDialog = () => {
  dialogVisible.value = false
}

const getConfigById = (id: number) => {
  ConfigApi.getDetailById(id).then((res) => {
    console.log(res)
    qrCodeLogo.value = res.data.logo
  })
}

// 获取 ProTable 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const proTable = ref()
const tableDataSize = ref(0)
// dataCallback 是对于返回的表格数据做处理，如果你后台返回的数据不是 list && total 这些字段，那么你可以在这里进行处理成这些字段
const dataCallback = (data: any) => {
  console.log(data)
  tableDataSize.value = data.total
  return {
    list: data.list,
    total: data.total
  }
}

const getTableList = (params: any) => {
  params.bookId = dialogProps.value.row.id
  params.type = dialogProps.value.type
  return BookResourceApi.page(params)
}

const columns: ColumnProps[] = [
  { type: 'selection', fixed: 'left', width: 60 },
  {
    prop: 'resourceName',
    label: '资源名',
    width: 200
  },
  {
    prop: 'link',
    label: '链接',
    width: 500
  },
  {
    prop: 'enabled',
    label: '状态',
    width: 150,
    render: (scope) => {
      return <el-tag type={scope.row.enabled === 1 ? 'danger' : 'success'}>{scope.row.enabled === 1 ? '失效' : '生效'}</el-tag>
    }
  },
  { prop: 'operation', label: '操作', fixed: 'right', width: 300 }
]

const dialogRef = ref()
const openDrawer = (title: string, row = {}) => {
  if (row.type === undefined) {
    row.type = dialogProps.value.type
  }
  console.log('row', row)

  let params = {
    title,
    row: { ...row, bookId: dialogProps.value.row.id },
    isView: title === '查看',
    api: title === '编辑' ? BookResourceApi.edit : BookResourceApi.add,
    getTableList: proTable.value.getTableList,
    maxHeight: '500px'
  }
  dialogRef.value.acceptParams(params)
}

let qrcode
let qrCodeLogo = ref()
const downloadQrcode = ref()
const showQrCodeFlag = ref(false)
const showQrCode = (row = {}) => {
  qrcode = row.qrLink
  qrRow.value = row
  showQrCodeFlag.value = true
}

const getImgInfo = (qrUrl: any) => {
  downloadQrcode.value = qrUrl
}

const fullscreenLoading = ref(false)
const qrRow = ref()
const downloadQrCode = () => {
  fullscreenLoading.value = true
  setTimeout(() => {
    fullscreenLoading.value = false
  }, 1500)
  let a = document.createElement('a')
  a.href = downloadQrcode.value
  a.setAttribute('download', qrRow.value.resourceName)
  a.click()
}

const cancelResourceDialog = () => {
  showQrCodeFlag.value = false
}

const downloadFile = async () => {
  ElMessageBox.confirm('确认下载全部资源?', '温馨提示', { type: 'warning' }).then(() => useDownload(BookResourceApi.download, '资源二维码', dialogProps.value.row.id, true, '.zip'))
}

const uploadTemplate = () => {
  window.location.href = 'http://demo.dianhuiyun.com.cn:9000/dsh/%E5%A4%9A%E8%B5%84%E6%BA%90%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx'
}

const importRes = ref()
const afterUpload = () => {
  if (importRes.value == 0) {
    proTable.value.getTableList()
  }
}

const deleteData = async (id: number) => {
  await useHandleData(BookResourceApi.remove, [id], `删除书籍资源`)
  proTable.value.getTableList()
}

const enabledChange = async (row = {}) => {
  try {
    await BookResourceApi.enabled(row)
    proTable.value.getTableList()
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('屏蔽操作失败:', error)
  }
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
