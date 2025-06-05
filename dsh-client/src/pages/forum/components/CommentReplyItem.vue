<template>
  <uni-card class="reply-card" :title="reply.username" :isFull="true" :extra="reply.createTime" :thumbnail="reply.avatar">
    <view class="content" v-if="reply.parentId === parentId">{{ reply.content }}</view>
    <view class="reply-item" v-else >
       回复
      <text style="color: rgb(33, 116, 212);">{{ reply.parentUsername }}</text>
      ：
      <text>{{ reply.content }}</text>
    </view>
    <view slot="actions" class="card-actions">
        <view class="card-actions-item" @click.stop="actionsClick(1)">
          <uni-icons v-if="!reply.like" type="heart" size="20" color="#999"></uni-icons>
          <uni-icons v-else type="heart-filled" size="20" color="red"></uni-icons>
        </view>
        <view class="card-actions-item" @click.stop="actionsClick(2)">
          <uni-icons v-if="!reply.star" type="star" size="20" color="#999"></uni-icons>
          <uni-icons v-else type="star-filled" size="20" color="#edd20a"></uni-icons>
        </view>
    </view>
  </uni-card>
</template>

<script setup>
import { doCommentAction } from '@/service/forum'

const props = defineProps({
  reply: {
    type: Object,
    require: true
  },
  parentId: {
    type: Number,
    require: true
  }
})

const actionsClick = async (params) => {
  // 1-点赞，2-收藏
  const { code } = await doCommentAction({
    commentId: props.reply.id,
    actionType: params
  })
  if (code !== 0) {
    uni.showToast({
      icon: 'error',
      duration: 300
    })
    return
  }
  if (params === 1) {
    // 点赞
    props.reply.like = !props.reply.like
  } else {
    props.reply.star = !props.reply.star
  }
  uni.showToast({duration: 300})
}
</script>

<style scoped lang="scss">
.card-actions {
  display: flex;
  padding: 10rpx 10rpx 0;
  justify-content: end;
  align-items: center;

  .card-actions-item {
    margin: 0 20rpx;
  }
}
</style>
