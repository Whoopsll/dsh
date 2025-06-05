<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" :cancel-dialog="cancelDialog">
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
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="dialogProps.row!.name" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="公众号Key" prop="account">
          <el-input v-model="dialogProps.row!.account" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="公众号Secret" prop="password">
          <el-input v-model="dialogProps.row!.password" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="资源码获取方式" prop="logo">
          <el-radio-group v-model="dialogProps.row!.resourceGetType">
            <el-radio :value="0">公众号获取</el-radio>
            <el-radio :value="1">书籍内获取</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公众号logo" prop="logo">
          <UploadImg v-model:image-url="dialogProps.row!.logo" width="135px" height="135px" :file-size="5">
            <template #empty>
              <el-icon><Avatar /></el-icon>
              <span>请上传logo</span>
            </template>
            <template #tip> 图片大小不能超过 5M </template>
          </UploadImg>
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
import { ref, reactive } from 'vue'
import { ElMessage, FormInstance } from 'element-plus'
import UploadImg from '@/components/Upload/Img.vue'
import { Dialog } from '@/components/Dialog'

interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  publisherId: number
  labelWidth?: number
  maxHeight?: number | string
  api?: (params: any) => Promise<any>
  getTableList?: () => Promise<any>
}
const dialogVisible = ref(false)
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: {},
  labelWidth: 160,
  publisherId: 0,
  fullscreen: false,
  maxHeight: '500px'
})

// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
}

defineExpose({
  acceptParams
})

const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  account: [{ required: true, message: '请输入微信公众号key', trigger: 'blur' }],
  password: [{ required: true, message: '请输入微信公众号Secret', trigger: 'blur' }],
  logo: [{ required: true, message: '请上传logo', trigger: 'blur' }]
  //   link: [
  //     {
  //       required: true,
  //       message: '链接不能为空',
  //       trigger: 'blur'
  //     },
  //     {
  //       validator: (rule, value, callback) => {
  //         const urlPattern = /^(https?:\/\/)([a-zA-Z0-9.-]+)(\.[a-zA-Z]{2,})(:[0-9]{1,5})?(\/.*)?$/
  //         if (!urlPattern.test(value)) {
  //           callback(new Error('请输入合法的链接'))
  //         } else {
  //           callback()
  //         }
  //       },
  //       trigger: 'blur'
  //     }
  //   ]
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      dialogProps.value.row.publisherId = dialogProps.value.publisherId
      await dialogProps.value.api!(dialogProps.value.row)
      ElMessage.success({ message: `${dialogProps.value.title}成功！` })
      dialogProps.value.getTableList!()
      dialogVisible.value = false
      ruleFormRef.value!.resetFields()
      cancelDialog(true)
    } catch (error) {
      console.log(error)
    }
  })
}
const cancelDialog = (isClean?: boolean) => {
  dialogVisible.value = false
  let condition = ['查看', '编辑']
  if (condition.includes(dialogProps.value.title) || isClean) {
    dialogProps.value.row = {}
    ruleFormRef.value!.resetFields()
  }
}
</script>

<style scoped lang="less"></style>
