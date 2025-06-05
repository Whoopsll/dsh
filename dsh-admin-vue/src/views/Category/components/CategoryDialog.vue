<template>
  <Dialog :model-value="dialogVisible" :maxHeight="200" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" :cancel-dialog="cancelDialog">
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
        <el-form-item label="上级分类" prop="parentId">
          <el-tree-select :data="dialogProps.typeList" v-model="dialogProps.row!.parentId" :props="defaultProps" check-strictly :render-after-expand="false" />
          <!-- <el-select v-model="dialogProps.row!.parentId" filterable placeholder="请选择" class="w-full">
            <el-option v-for="item in dialogProps.typeList" :key="item.id" :label="item.name" :value="item.id" class="isabel-option" :disabled="item.id == dialogProps.row!.id" />
          </el-select> -->
        </el-form-item>
        <el-form-item label="分类名" prop="name">
          <el-input v-model="dialogProps.row!.name" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input v-model="dialogProps.row!.description" placeholder="" clearable></el-input>
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
import { CategoryApi } from '@/api/modules/category'
interface DialogProps {
  title: string
  isView: boolean
  fullscreen?: boolean
  row: any
  labelWidth?: number
  maxHeight?: number | string
  api?: (params: any) => Promise<any>
  getTableList?: () => Promise<any>
  typeList?: any
}
const dialogVisible = ref(false)
const dialogProps = ref<DialogProps>({
  isView: false,
  title: '',
  row: { type: 'MENU', openType: 'TAB', keepalive: false, hide: false, sort: 0 },
  labelWidth: 160,
  fullscreen: false
})

const defaultProps = ref({
  children: 'children',
  label: 'name',
  value: 'id'
})

// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  getSelectTypeList()
}

const getSelectTypeList = async () => {
  var param = {
    page: 1,
    limit: 100
  }
  const { data } = await CategoryApi.page(param)
  dialogProps.value.typeList = data
  console.log(dialogProps.value.typeList)
}

defineExpose({
  acceptParams
})

const rules = reactive({
  name: [{ required: true, message: '请输入分类名', trigger: 'blur' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
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
    dialogProps.value.row = {}
    ruleFormRef.value!.resetFields()
  }
}
</script>

<style scoped lang="less"></style>
