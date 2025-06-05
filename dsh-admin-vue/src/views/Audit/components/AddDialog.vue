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
        <el-form-item label="审核周期" prop="type">
          <el-select v-model="dialogProps.row!.interval" clearable placeholder="选择审核审核周期" style="width: 400px">
            <el-option v-for="item in intervalList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行日期" v-if="dialogProps.row.interval == 1">
          <el-checkbox-group v-model="dialogProps.row!.date" @change="handleDateChange">
            <el-checkbox label="周一" value="1"></el-checkbox>
            <el-checkbox label="周二" value="2"></el-checkbox>
            <el-checkbox label="周三" value="3"></el-checkbox>
            <el-checkbox label="周四" value="4"></el-checkbox>
            <el-checkbox label="周五" value="5"></el-checkbox>
            <el-checkbox label="周六" value="6"></el-checkbox>
            <el-checkbox label="周日" value="7"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="执行时间" v-if="dialogProps.row.interval == 1">
          <el-time-picker value-format="HH:mm" format="HH:mm" v-model="dialogProps.row!.time" placeholder="请选择执行时间" />
        </el-form-item>
        <el-form-item label="处理邮箱地址" prop="email">
          <el-input clearable v-model="dialogProps.row.email" placeholder="请输入处理异常的邮箱通知地址" style="width: 400px" />
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

const intervalList = ref([
  {
    name: '立刻执行',
    value: 0
  },
  {
    name: '每周',
    value: 1
  }
])

onMounted(() => {})

const handleDateChange = (value: number[]) => {
  console.log(value)
  console.log(dialogProps.value.row.date)
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
  interval: [{ required: true, message: '请选择审核周期', trigger: 'blur' }]
})
const ruleFormRef = ref<FormInstance>()
const handleSubmit = () => {
  console.log(dialogProps.value.row)
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      // 修改定时任务配置
      var param = dialogProps.value.row.taskInfo
      if (dialogProps.value.row.interval == 0) {
        // 立刻执行
        param.executionTime = ''
      } else {
        // 设置为每周几执行
        var date = dialogProps.value.row.date
        var times = dialogProps.value.row.time.split(':')
        var dates = [0, 0, 0, 0, 0, 0, 0]
        var executionTime = ''
        for (var i = 0; i < date.length; i++) {
          // 将选中的日期先转换为数据库存放的字段格式
          dates[Number.parseInt(date[i]) - 1] = 1
        }
        for (var i = 0; i < dates.length; i++) {
          if (i == dates.length - 1) {
            // 最后一位是星期几，需要单独处理
            executionTime += dates[i] + ',' + times[0] + ',' + times[1]
          } else {
            executionTime += dates[i] + ','
          }
        }
        param.executionTime = executionTime
      }
      param.email = dialogProps.value.row.email
      await dialogProps.value.api!(param)
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
