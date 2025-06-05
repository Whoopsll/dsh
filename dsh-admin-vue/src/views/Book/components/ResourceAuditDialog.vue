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
        <el-form-item label="资源码地址" prop="bookName">
          <el-input v-model="dialogProps.row!.bookName" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="资源二维码" prop="bookCover">
          <UploadImg v-model:image-url="dialogProps.row!.bookCover" width="135px" height="135px" :file-size="5">
            <template #empty>
              <el-icon><Avatar /></el-icon>
              <span>请上传资源码</span>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, FormInstance } from 'element-plus'
import { Dialog } from '@/components/Dialog'
import UploadImg from '@/components/Upload/Img.vue'
import { CategoryApi } from '@/api/modules/category'
import { PublisherApi } from '@/api/modules/publisher'
import { useAppStoreWithOut } from '@/store/modules/app'

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
  row: {},
  labelWidth: 160,
  fullscreen: false,
  maxHeight: '300px'
})

// 获取分类列表
const classificationList = ref()
const idsList = ref<number[]>([])
const getClassificationList = async () => {
  var param = {
    page: 1,
    limit: 100
  }
  const res = await CategoryApi.page(param)
  res.data.forEach((item) => {
    idsList.value.push(item.id)
  })
  classificationList.value = res.data
}
// 获取出版社列表
const publisherList = ref()
const appStore = useAppStoreWithOut()
const getPublisherList = async () => {
  const res = await PublisherApi.list()
  publisherList.value = res.data.filter((item) => (appStore.userInfo.roleId === 1 ? true : appStore.publisherIdList.includes(item.id)))
  console.log(publisherList.value)
}

onMounted(() => {
  getClassificationList()
  getPublisherList()
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

const rules = reactive({})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      if (dialogProps.value.row.bookCover == '' || dialogProps.value.row.bookCover == null) {
        dialogProps.value.row.bookCover = 'https://my-egg.oss-cn-beijing.aliyuncs.com/default_cover.png'
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
