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
        <el-form-item label="论坛封面" prop="cover">
          <UploadImg v-model:image-url="dialogProps.row!.cover" width="135px" height="135px" :file-size="5">
            <template #empty>
              <el-icon><Avatar /></el-icon>
              <span>请上传封面</span>
            </template>
            <template #tip> 图片大小不能超过 5M </template>
          </UploadImg>
        </el-form-item>
        <el-form-item label="论坛名" prop="name">
          <el-input v-model="dialogProps.row!.name" style="width: 300px" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="绑定书籍" prop="bookId">
          <el-select v-model="dialogProps.row!.bookId" clearable placeholder="绑定书籍" style="width: 300px" filterable :disabled="isEditStatus">
            <el-option v-for="item in bookList" :key="item.id" :label="item.bookName" :value="item.id">
              <el-tooltip placement="top" :hide-after="0">
                <template #content> {{ item.bookName }} </template>
                <el-text style="float: left; width: 40%" line-clamp="1" truncated>{{ item.bookName }}</el-text>
              </el-tooltip>
              <el-tooltip placement="top" :hide-after="0">
                <template #content> {{ item.isbn }} </template>
                <el-text style="float: right; color: var(--el-text-color-secondary); font-size: 13px; width: 40%" line-clamp="1" truncated>
                  {{ item.isbn }}
                </el-text>
              </el-tooltip>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="绑定圈主" prop="userId">
          <el-select v-model="dialogProps.row!.userId" clearable placeholder="绑定圈主" style="width: 300px">
            <el-option v-for="item in userList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="作者答缺省价" prop="price">
          <el-input-number v-model="dialogProps.row!.price" style="width: 300px" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
        <el-form-item label="课程链接" prop="courseLink">
          <el-input v-model="dialogProps.row!.courseLink" style="width: 300px" placeholder="" clearable></el-input>
        </el-form-item>
        <el-form-item label="作者简介" prop="writerIntroduction">
          <el-input v-model="dialogProps.row!.writerIntroduction" style="width: 300px" :autosize="{ minRows: 2 }" type="textarea" placeholder="请输入作者简介" clearable />
        </el-form-item>
        <el-form-item label="语音介绍" prop="voiceIntroduction">
          <UploadVoice v-model:voice-url="dialogProps.row!.voiceIntroduction" width="300px" height="60px" :file-size="10">
            <template #empty>
              <el-icon><Upload /></el-icon>
              <span>上传语音介绍</span>
            </template>
          </UploadVoice>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <UploadImg v-model:image-url="dialogProps.row!.avatar" width="135px" height="135px" :file-size="5">
            <template #empty>
              <el-icon><Avatar /></el-icon>
              <span>请上传头像</span>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, FormInstance } from 'element-plus'
import { Dialog } from '@/components/Dialog'
import UploadImg from '@/components/Upload/Img.vue'
import UploadVoice from '@/components/Upload/Voice.vue'
import { UserApi } from '@/api/modules/user'
import { BookApi } from '@/api/modules/book'

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
  maxHeight: '500px'
})

const isEditStatus = computed(() => {
  return dialogProps.value.row.id !== null && dialogProps.value.row.id !== undefined && dialogProps.value.row.id > 0
})

// 获取用户列表
const userList = ref()
const getUserList = async () => {
  const res = await UserApi.list()
  userList.value = res.data
}
const bookList = ref()
const getBookList = async () => {
  const res = await BookApi.list()
  bookList.value = res.data
}
onMounted(() => {
  getUserList()
  getBookList()
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
  cover: [{ required: true, message: '请上传论坛封面', trigger: 'change' }],
  name: [{ required: true, message: '请输入论坛名', trigger: 'blur' }],
  bookId: [{ required: true, message: '请绑定书籍', trigger: 'change' }],
  userId: [{ required: true, message: '请绑定圈主', trigger: 'change' }],
  price: [{ required: true, message: '请输入作者答缺省价', trigger: 'change' }],
  writerIntroduction: [{ required: true, message: '请输入作者简介', trigger: 'blur' }]
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
