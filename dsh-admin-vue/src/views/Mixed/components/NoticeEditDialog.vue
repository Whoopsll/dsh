<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" :max-height="300" :cancel-dialog="cancelDialog">
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
        <el-form-item label="标题" prop="title">
          <el-input v-model="dialogProps.row!.title" placeholder="请输入标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="dialogProps.row!.content" :autosize="{ minRows: 2 }" type="textarea" placeholder="请输入内容" clearable></el-input>
        </el-form-item>
        <el-form-item label="生效时间" prop="validTime">
          <el-date-picker v-model="dialogProps.row!.validTime" type="date" :disabled-date="disabledDate" placeholder="选择生效时间" clearable />
        </el-form-item>
        <el-form-item label="是否置顶" prop="isTop">
          <el-radio-group v-model="dialogProps.row!.isTop" size="large">
            <el-radio-button label="默认" :value="0" />
            <el-radio-button label="置顶" :value="1" />
          </el-radio-group>
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
import { Dialog } from '@/components/Dialog'

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
const dialogVisible = ref(false)
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: {
    isTop: 0
  },
  labelWidth: 160,
  fullscreen: false,
  maxHeight: '500px'
})

const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 1000 * 60 * 60 * 24
}

// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  console.log(params)
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
}

defineExpose({
  acceptParams
})

const rules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  validTime: [{ required: true, message: '请选择时间', trigger: 'change' }]
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
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
  })
}
const cancelDialog = (isClean?: boolean) => {
  dialogVisible.value = false
  let condition = ['查看', '编辑']
  if (condition.includes(dialogProps.value.title) || isClean) {
    dialogProps.value.row = {
      isTop: 0
    }
    ruleFormRef.value!.resetFields()
  }
}
</script>

<style scoped lang="less"></style>
