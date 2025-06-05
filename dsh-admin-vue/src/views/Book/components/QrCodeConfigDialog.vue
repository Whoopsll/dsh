<template>
  <Dialog :model-value="dialogVisible" :title="dialogProps.title" :fullscreen="dialogProps.fullscreen" :max-height="dialogProps.maxHeight" :cancel-dialog="cancelDialog">
    <div :style="'width: calc(100% - ' + dialogProps.labelWidth! / 2 + 'px)'">
      <el-form
        ref="ruleFormRef"
        label-position="right"
        :label-width="dialogProps.labelWidth + 'px'"
        :rules="generateType == 1 ? formRules : rules"
        :model="dialogProps.row"
        :disabled="dialogProps.isView"
        :hide-required-asterisk="dialogProps.isView"
      >
        <el-form-item label="生成方式">
          <el-radio-group v-model="generateType" @change="handleGenerateType">
            <el-radio :value="0">默认方式</el-radio>
            <el-radio :value="1">公众号方式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出版社" prop="publisherId" v-show="generateType == 1">
          <el-select v-model="dialogProps.row!.publisherId" clearable placeholder="选择出版社" style="width: 400px" @change="handlePublisherChange">
            <el-option v-for="item in publisherList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="公众号" prop="wxConfigId" v-show="generateType == 1">
          <el-select v-model="dialogProps.row!.wxConfigId" clearable placeholder="选择公众号" style="width: 400px">
            <el-option v-for="item in wxConfigs" :key="item.id" :label="item.name" :value="item.id" />
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
import { ConfigApi } from '@/api/modules/config'
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
//监听出版社的选择
const handlePublisherChange = () => {
  getWxConfig()
}
// // 监听微信公众号选择
// const handleWxKeyChange = (index: number) => {
//   dialogProps.value.row.id = wxConfigs.value[index].id
// }
const generateType = ref(0)
// 获取出版社列表
const publisherList = ref()
const appStore = useAppStoreWithOut()
const getPublisherList = async () => {
  const res = await PublisherApi.list()
  publisherList.value = res.data.filter((item) => (appStore.userInfo.roleId === 1 ? true : appStore.publisherIdList.includes(item.id)))
  if (dialogProps.value.row.publisherId == 0) {
    dialogProps.value.row.publisherId = publisherList.value[0].id
  }
}
//监听二维码生成类型选择
const handleGenerateType = () => {
  if (generateType.value == 0) {
    dialogProps.value.row.wxConfigId = 0
  } else {
    dialogProps.value.row.wxConfigId = wxConfigs.value[0].id
  }
}

onMounted(() => {
  getPublisherList()
})

// 接收父组件传过来的参数
const acceptParams = (params: DialogProps): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  if (dialogProps.value.row.publisherId == 0) {
    dialogProps.value.row.publisherId = publisherList.value[0].id
  }
  if (dialogProps.value.row.wxConfigId == 0) {
    generateType.value = 0
  } else {
    generateType.value = 1
  }
  getWxConfig()
}
const wxConfigs = ref([])
const getWxConfig = () => {
  // 获取公众号信息
  ConfigApi.page({ page: 1, limit: 100, publisherId: dialogProps.value.row.publisherId }).then((res) => {
    wxConfigs.value = res.data.list
    if (wxConfigs.value.length > 0) {
      dialogProps.value.row.wxConfigId = wxConfigs.value[0].id
    }
  })
}

defineExpose({
  acceptParams
})

const rules = reactive({})
const formRules = reactive({
  publisherId: [{ required: true, message: '请选择出版社', trigger: 'blur' }],
  wxConfigId: [{ required: true, message: '请选择公众号,若未配置可到出版社管理中配置', trigger: 'blur' }]
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
