<template>
  <Dialog :model-value="dialogVisible" top="8vh" :title="dialogProps.title" width="75vw" :cancel-dialog="cancelDialog">
    <div class="container">
      <Comment-item :control="false" :isHighLight="true" :commentItem="dialogProps.row" />
      <el-divider />

      <template v-for="(item, index) in dialogProps.row.replyList" :key="index">
        <Comment-item :commentItem="item" />
        <el-divider />
      </template>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { Dialog } from '@/components/Dialog'
import { ref } from 'vue'
import CommentItem from '@/components/CommentItem/index.vue'

const dialogVisible = ref(false)
const dialogProps = ref({
  isView: false,
  title: '评论详情',
  row: {},
  labelWidth: 160,
  fullscreen: false,
  maxHeight: '500px'
})

// 接收父组件传过来的参数
const acceptParams = (params): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  console.log(dialogProps.value)
}

defineExpose({
  acceptParams
})

const cancelDialog = () => {
  dialogVisible.value = false
}
</script>

<style scoped lang="less">
.container {
  padding: 10px;
}
</style>
