<template>
  <Dialog :model-value="dialogVisible" top="8vh" :title="dialogProps.title" width="75vw" :cancel-dialog="cancelDialog">
    <el-container>
      <el-row :gutter="20">
        <el-col :span="10" :offset="0">
          <el-descriptions :column="2" size="large" direction="vertical" border>
            <el-descriptions-item label="封面">
              <el-image :src="dialogProps.row.cover"></el-image>
            </el-descriptions-item>
            <el-descriptions-item label="头像">
              <el-image :src="dialogProps.row.avatar"></el-image>
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="14" :offset="0">
          <el-row>
            <el-descriptions :column="1" size="large" border>
              <el-descriptions-item min-width="300" label="ID">
                <el-text>{{ dialogProps.row.id }}</el-text>
              </el-descriptions-item>
              <el-descriptions-item label="论坛名">
                <el-text>{{ dialogProps.row.name }}</el-text>
              </el-descriptions-item>
              <el-descriptions-item min-width="300" label="参与人数">
                <el-text>{{ dialogProps.row.userNumber }}</el-text>
              </el-descriptions-item>
              <el-descriptions-item min-width="300" label="创建时间">
                <el-text>{{ dialogProps.row.createTime }}</el-text>
              </el-descriptions-item>
            </el-descriptions>
          </el-row>
          <el-row class="mt-5">
            <el-descriptions :column="1" size="large" border>
              <el-descriptions-item min-width="300" label="作者介绍">
                <el-text>{{ dialogProps.row.writerIntroduction }}</el-text>
              </el-descriptions-item>
              <el-descriptions-item label="圈子介绍" min-width="300">
                <audio :src="dialogProps.row.voiceIntroduction" controls></audio>
              </el-descriptions-item>
            </el-descriptions>
          </el-row>
        </el-col>
      </el-row>
    </el-container>
    <el-container style="margin-top: 50px">
      <el-row :gutter="20">
        <el-row v-for="(item, index) in commentList" :key="index">
          <Comment-item :commentItem="item" @remove="confirmRemove" @detail="showDetail" @enabled="handleEnabled" />
        </el-row>
      </el-row>
    </el-container>
  </Dialog>
  <CommentDetailDialog ref="detailDialogRef" />
</template>

<script setup lang="ts">
import { Dialog } from '@/components/Dialog'
import { ref } from 'vue'
import { CommentApi } from '@/api/modules/mixed'
import CommentItem from '@/components/CommentItem/index.vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import CommentDetailDialog from './CommentDetailDialog.vue'

const dialogVisible = ref(false)
const dialogProps = ref({
  isView: false,
  title: '圈子详情',
  row: {},
  labelWidth: 160,
  fullscreen: false,
  maxHeight: '500px'
})

// 接收父组件传过来的参数
const acceptParams = (params): void => {
  params.row = { ...dialogProps.value.row, ...params.row }
  dialogProps.value.title = params.row.name
  dialogProps.value = { ...dialogProps.value, ...params }
  dialogVisible.value = true
  queryCommentList(params.row.id)
}

defineExpose({
  acceptParams
})

const cancelDialog = () => {
  dialogVisible.value = false
}

const commentList = ref()
const queryCommentList = async (forumId: number) => {
  console.log(forumId)
  const { data } = await CommentApi.list(forumId)
  commentList.value = data
}

const confirmRemove = (commentId: number) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      handleRemove(commentId)
    })
    .catch(() => {
      ElMessage.info('取消删除')
    })
}

const handleRemove = async (commentId: number) => {
  try {
    await CommentApi.remove(commentId)
    // 删除成功后，更新评论列表
    queryCommentList(dialogProps.value.row.id)
    ElMessage.success('删除成功')
  } catch (error) {
    console.error('删除评论失败:', error)
  }
}

const handleEnabled = async (commentId: number) => {
  try {
    await CommentApi.enabled(commentId)
    // 删除成功后，更新评论列表
    queryCommentList(dialogProps.value.row.id)
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('屏蔽操作失败:', error)
  }
}

const detailDialogRef = ref()
const showDetail = (row = {}) => {
  let params = {
    row: { ...row }
  }
  detailDialogRef.value.acceptParams(params)
}
</script>

<style scoped></style>
