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
        <el-form-item label="审核服务名称" prop="name">
          <el-input v-model="dialogProps.row!.name" clearable disabled placeholder="" style="width: 400px" />
        </el-form-item>
        <el-form-item label="审核类型" prop="type">
          <el-select v-model="dialogProps.row!.type" clearable placeholder="选择审核类型" @change="handleTypeChange" style="width: 400px">
            <el-option v-for="item in types" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="运行状态" prop="status">
          <el-select v-model="dialogProps.row!.status" clearable placeholder="选择运行状态" style="width: 400px">
            <el-option v-for="item in statuList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务地址" prop="serviceAddress">
          <el-input clearable v-model="dialogProps.row!.serviceAddress" placeholder="请输入服务地址" style="width: 400px" />
        </el-form-item>
        <el-form-item label="用户名" prop="account">
          <el-input clearable v-model="dialogProps.row!.account" placeholder="请输入用户名" style="width: 400px" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input clearable v-model="dialogProps.row!.password" placeholder="请输入密码" style="width: 400px" />
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

const types = ref([
  {
    name: '外部地址内容审核',
    value: 1
  },
  {
    name: '第三方资源网址内容审核',
    value: 2
  }
])

const statuList = ref([
  {
    name: '运行',
    value: 1
  },
  {
    name: '停止',
    value: 0
  }
])

onMounted(() => {})

const handleTypeChange = (value: number) => {
  dialogProps.value.row.name = types.value.find((item) => item.value === value)!.name || ''
}

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
  type: [{ required: true, message: '请选择审核类型', trigger: 'blur' }],
  status: [{ required: true, message: '请选择运行状态', trigger: 'blur' }]
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      if (dialogProps.value.row.serviceType == null) {
        dialogProps.value.row.serviceType = 0
        dialogProps.value.row.executionTime = ''
      }
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
