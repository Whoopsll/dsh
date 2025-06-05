<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" :max-height="dialogProps.maxHeight" :cancel-dialog="cancelDialog">
    <div :style="'width: calc(100% - ' + dialogProps.labelWidth! / 2 + 'px)'">
      <el-form
        ref="ruleFormRef"
        label-position="right"
        :label-width="dialogProps.labelWidth + 'px'"
        :rules="rules"
        :model="dialogProps.row"
        :disabled="dialogProps.isView"
        :hide-required-asterisk="dialogProps.isView"
      >
        <el-form-item label="审核名称" prop="name">
          <el-input v-model="dialogProps.row!.name" clearable placeholder="" style="width: 400px" />
        </el-form-item>
        <el-form-item label="执行类型" prop="executeType">
          <el-select v-model="dialogProps.row!.executeType" clearable placeholder="选择执行类型" style="width: 400px">
            <el-option v-for="item in executeType" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间" v-if="dialogProps.row.executeType == 1">
          <el-date-picker value-format="YYYY-MM-DD HH:mm:ss" format="YYYY-MM-DD HH:mm:ss" type="datetime" v-model="dialogProps.row!.auditStartTime" placeholder="请选择执行时间" />
        </el-form-item>
        <el-form-item label="审核文件">
          <el-upload
            ref="upload"
            v-model:file-list="fileList"
            class="upload-demo"
            :action="baseUrl + 'dsh-admin-api/common/upload/file'"
            :auto-upload="false"
            :headers="uploadHeaders"
            :on-change="handleChange"
            :on-success="handleSuccess"
            :accept="dialogProps.row.type == 1 || dialogProps.row.type == 0 ? 'video/*' : '*/*'"
            :on-error="handleError"
          >
            <el-button type="primary">上传文件</el-button>
            <template #tip>
              <div class="el-upload__tip">文件大小不得超过50MB</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="审核状态" prop="executeStatus" v-if="dialogProps.isView">
          <el-select v-model="dialogProps.row!.executeStatus" disabled clearable placeholder="选择审核状态" style="width: 400px">
            <el-option v-for="item in executeStatus" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核结果" prop="executeResult" v-if="dialogProps.isView">
          <el-select v-model="dialogProps.row!.executeResult" clearable placeholder="选择审核结果" disabled style="width: 400px">
            <el-option v-for="item in executeResult" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <slot name="footer">
        <el-button @click="cancelDialog">取消</el-button>
        <el-button type="primary" v-show="!dialogProps.isView" @click="handleSubmit">确定</el-button>
      </slot>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, FormInstance } from 'element-plus'
import { Dialog } from '@/components/Dialog'
import { useAppStoreWithOut } from '@/store/modules/app'
import { executeType, executeResult, executeStatus } from '@/configs/enum'

interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  labelWidth?: number
  maginTop?: string
  maxHeight?: number | string
  api?: (params: any) => Promise<any>
  getTableList?: () => Promise<any>
}
const baseUrl = import.meta.env.VITE_API_URL as string
const fileList = ref([])
const dialogVisible = ref(false)
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: {},
  labelWidth: 160,
  marginTop: '8vh',
  fullscreen: false,
  maxHeight: '500px'
})

onMounted(() => {})

// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  if (dialogProps.value.row.localFileInfo != '' && dialogProps.value.row.localFileInfo != undefined) {
    fileList.value = JSON.parse(dialogProps.value.row.localFileInfo)
  }
}

defineExpose({
  acceptParams
})
const isHandleChange = ref(false) //记录上传文件是否发生变化
//监听文件选择按钮
const handleChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
  fileList.value = uploadFiles
  isHandleChange.value = true
  console.log(uploadFiles)
}
// 监听上传成功
const handleSuccess = (response, file, files) => {
  console.log('上传成功:', response, file, files)
  if (response.code == 0) {
    let fileUrl = ''
    //上传成功,提交添加审核信息
    if (response.data.urlList.length > 0) {
      fileUrl = response.data.urlList[0]
    }
    if (fileUrl == '' || fileUrl == undefined) {
      ElMessage.success({ message: `审核的文件不能为空` })
      return
    }
    saveAuditInfo(files, fileUrl)
  }
}

//上传文件
const saveAuditInfo = (files, fileUrl) => {
  if (files.length > 0) {
    dialogProps.value.row.localFileInfo = JSON.stringify(files)
    dialogProps.value.row.fileSize = (files[0].size / 1024).toFixed(2)
    dialogProps.value.row.fileName = files[0].name
  }
  if (fileUrl != '') {
    dialogProps.value.row.url = fileUrl
  }
  //提交新增信息
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      await dialogProps.value.api!(dialogProps.value.row)
      ElMessage.success({ message: `${dialogProps.value.title}成功！` })
      dialogProps.value.getTableList!()
      dialogVisible.value = false
      ruleFormRef.value!.resetFields()
      cancelDialog(true)
    } catch (error) {
      console.log(error)
    }
    upload.value.clearFiles()
  })
}
// 监听上传失败
const handleError = (err, file, files) => {
  console.error('上传失败:', err, file, files)
  ElMessage.success({ message: `文件上传异常`, type: 'warning' })
}
//上传控件的ref
const upload = ref()
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  executeType: [{ required: true, message: '请选择执行类型', trigger: 'blur' }]
})
// 定义 headers，包含 Authorization Token
const uploadHeaders = ref({
  authorization: useAppStoreWithOut().token
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  //先上传文件，文件上传成功，新增审核信息
  if (isHandleChange.value) {
    upload.value.submit()
  } else {
    saveAuditInfo('', '')
  }
}
const cancelDialog = (isClean?: boolean) => {
  dialogVisible.value = false
  let condition = ['查看', '编辑']
  if (condition.includes(dialogProps.value.title) || isClean) {
    dialogProps.value.row = {}
    ruleFormRef.value!.resetFields()
  }
  upload.value.clearFiles()
}
</script>

<style scoped lang="less"></style>
