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
        <el-form-item label="书名" prop="bookName">
          <el-input v-model="dialogProps.row!.bookName" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="dialogProps.row!.isbn" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="classificationId">
          <!-- <el-select v-model="dialogProps.row!.classificationId" clearable placeholder="选择书籍分类" style="width: 400px">
            <el-option v-for="item in classificationList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select> -->
          <el-tree-select
            :data="classificationList"
            style="width: 400px"
            v-model="dialogProps.row!.classificationId"
            placeholder="选择书籍分类"
            :props="defaultProps"
            :default-checked-keys="idsList"
            default-expand-all
            :node-key="id"
            check-strictly
            :render-after-expand="false"
          />
        </el-form-item>
        <el-form-item label="出版社" prop="publisherId">
          <el-select v-model="dialogProps.row!.publisherId" clearable placeholder="选择出版社" style="width: 400px">
            <el-option v-for="item in publisherList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="译者" prop="translator">
          <el-input v-model="dialogProps.row!.translator" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="writer">
          <el-input v-model="dialogProps.row!.writer" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="售价" prop="price">
          <el-input-number v-model="dialogProps.row!.price" :precision="2" :step="0.1" :min="0" style="width: 400px" />
        </el-form-item>
        <el-form-item label="页数" prop="pageNumber">
          <el-input-number v-model="dialogProps.row!.pageNumber" :step="1" style="width: 400px" />
        </el-form-item>
        <el-form-item label="装帧" prop="bookStyle">
          <el-input v-model="dialogProps.row!.bookStyle" placeholder="" clearable style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="出版时间" prop="publishDate">
          <el-date-picker
            v-model="dialogProps.row!.publishDate"
            style="width: 400px"
            type="date"
            placeholder="选择出版时间"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            clearable
          />
        </el-form-item>
        <el-form-item label="作者简介" prop="authorIntroduction">
          <el-input v-model="dialogProps.row!.authorIntroduction" style="width: 400px" :autosize="{ minRows: 2 }" type="textarea" placeholder="请输入作者简介" clearable />
        </el-form-item>
        <el-form-item label="书本简介" prop="bookIntroduction">
          <el-input v-model="dialogProps.row!.bookIntroduction" style="width: 400px" :autosize="{ minRows: 2 }" type="textarea" placeholder="请输入书本简介" clearable />
        </el-form-item>
        <el-form-item label="论坛链接" prop="forumLink" v-show="dialogProps.isView">
          <el-input v-model="dialogProps.row!.forumLink" style="width: 400px" placeholder="请创建对应书籍论坛后查看！" clearable disabled></el-input>
        </el-form-item>
        <el-form-item label="书籍封面" prop="bookCover">
          <UploadImg v-model:image-url="dialogProps.row!.bookCover" width="135px" height="135px" :file-size="5">
            <template #empty>
              <el-icon><Avatar /></el-icon>
              <span>请上传封面</span>
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
  maxHeight: '500px'
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

const defaultProps = ref({
  children: 'children',
  label: 'name',
  value: 'id'
})

const rules = reactive({
  bookName: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  writer: [{ required: true, message: '请输入作者', trigger: 'blur' }]
})
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
