<template>
  <view style="padding-left: 20rpx;">
    <view class="head">
      <image class="cover" :src="comment.avatar" mode="aspectFit"></image>
      <text>{{comment.username}}</text>
    </view>
    <view class="comment">
      <view class="content">
           <transition name="expand">
              <text class="content-text" :class="{ 'clamped': !data.isExpandedContent }" :style="dynamicStyle">{{comment.content}}</text>
           </transition>
           <view v-if="comment.fileType == 0 && comment.files.length > 0 && (comment.content.length < 60 || data.isExpandedContent)" style="display: flex; flex-wrap: wrap; margin-top: 10rpx;">
              <view class="image-card" v-for="(item, index) in comment.files" @click="previewCommentImage(index,comment.files)" :key="index">
                 <image :src="item"></image>
              </view>
           </view>
           <view class="expanded" v-if="comment.content.length > 60" @click="handleExpandedContent()">
            <text>{{data.isExpandedContent ? '收起' : '展开'}}</text>
            <image v-if="data.isExpandedContent" src="@/static/notice/icon_close.png"></image>
            <image v-else src="@/static/notice/icon_expanded.png"></image>
           </view>
      </view>
      <view class="bottom">
          <text class="time">{{comment.createTime}}</text>
          <view class="btn" v-if="6 != comment.userId">
             <view @click="actionsClick(1)">
                <image v-show="!comment.like" src="@/static/notice/icon_like_unselect.png"></image>
                <image v-show="comment.like" src="@/static/notice/icon_like_selected.png"></image>
                <text>{{comment.likeNum}}</text>
             </view>
             <view @click="handleComment(comment.id)">
                <image src="@/static/notice/icon_comment.png"></image>
                <text>{{comment.replyList.length}}</text>
             </view>
          </view>
      </view>
    </view>
    <view class="reply" v-if="comment.replyList != null && comment.replyList.length > 0 ">
        <view v-for="(item, index) in comment.replyList.slice(0, data.isExpandedReplay ? comment.replyList.length : 3)" style="display: flex; flex-direction: column;" :key="index">
             <p style="font-size: 26rpx;" @click="handleComment(item.id)">
                <text style="color: #1dc09d;">{{item.username}}</text>回复<text style="color: #1dc09d;">{{item.parentUsername}}</text>:{{item.content}}
             </p>
             <view v-if="item.fileType == 0 && item.files != null" style="display: flex; flex-wrap: wrap; margin-top: 10rpx;">
              <view class="image-card" v-for="(item1, index) in item.files" :key="index" @click="previewCommentImage(index, item.files)">
                 <image :src="item1" model="aspectFill"></image>
              </view>
           </view>
        </view>
        <view class="expanded" v-if="comment.replyList.length > 3" @click="handleExpandedReply()">
          <text>{{data.isExpandedReplay ? '收起' : '展开'}}</text>
          <image v-if="data.isExpandedReplay" src="@/static/notice/icon_close.png"></image>
          <image v-else src="@/static/notice/icon_expanded.png"></image>
        </view>
    </view>
  </view>
</template>

<script setup>
import { doCommentAction } from '@/service/forum'
import { ref,computed } from 'vue'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
  showReply: {
    type: Boolean,
    default: true
  },
  clickable: {
    type: Boolean,
    default: true
  }
})
const data = ref({
  isExpandedReplay: false,
  isExpandedContent: false
})
const useUser = useUserStore();
const emit = defineEmits(['handleComment']);

const handleComment = (id) => {
  emit('handleComment', id)
}
const handleExpandedReply = () => {
  data.value.isExpandedReplay = !data.value.isExpandedReplay
}

const handleExpandedContent = () => {
  data.value.isExpandedContent = !data.value.isExpandedContent
}
const actionsClick = async (params) => {
  // 1-点赞，2-收藏
  const { code } = await doCommentAction({
    commentId: props.comment.id,
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
    props.comment.like = !props.comment.like
    props.comment.like ? props.comment.likeNum++ : props.comment.likeNum--
  } else {
    props.comment.star = !props.comment.star
  }
}

const dynamicStyle = computed(() => ({
  height: data.value.isExpandedContent ? 'auto' :  props.comment.content.length > 60 ? '90rpx' : 'auto',
  transition: 'height 0.3s ease'
}))

const previewCommentImage = (index, files) => {
  uni.previewImage({
                urls: files,
                current: index, // 当前显示图片的索引
                indicator: 'default' // 图片指示器样式，默认为圆点
            })
}
</script>

<style lang="scss" scoped>
.head {
  margin-top: 30rpx;
    display: flex;
    align-items: center;
    
    .cover {
      width: 60rpx;
      height: 60rpx;
      border-radius: 30rpx;
    }

    text {
      font-size: 26rpx;
      margin-left: 10rpx;
    }
  }
.comment {
  padding-left: 75rpx;

  .content {
    margin-top: 10rpx;
    display: flex;
    flex-direction: column;

    .content-text {
      font-size: 26rpx;
      overflow: hidden;
      color: black;
      display: -webkit-box;
      -webkit-box-orient: vertical;
    }
  }

  .bottom {
    display: flex;
    margin-top: 20rpx;
    margin-bottom: 20rpx;
    justify-content: space-between;

    .time {
      font-size: 26rpx;
      color: #e1e1e1;
    }

    .btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;

      view {
        font-size: 24rpx;
        display: flex;
        margin-left: 40rpx;
        align-items: center;

        image {
          width: 28rpx;
          height: 28rpx;
          margin-right: 10rpx;
        }
      }
    }
  }
}

.reply {
  margin-left: 75rpx;
  padding: 10rpx 15rpx 15rpx 15rpx;
  border-radius: 5rpx;
  background-color: #f6f6f9;

  view {
    display: flex;
    margin-top: 10rpx;
    
    p {
      font-size: 26rpx;
    }
  }
}

.clamped {
  -webkit-line-clamp: 3;
  line-clamp: 3;
}

.expanded {
  font-size: 26rpx;
  color: #1dc09d;
  display: flex;
  justify-content: flex-end;
  flex: 1;
  align-items: center;
  margin-top: 10rpx;

  image {
    width: 24rpx;
    height: 14rpx; 
    margin-left: 10rpx;
  }
}

/* 设置 transition 动画 */
.expand-enter-active, .expand-leave-active {
  transition: max-height 0.3s ease;
}

/* 当进入时的初始高度 */
.expand-enter, .expand-leave-to {
  max-height: 0;
  overflow: hidden;
}


.image-card {
      flex: 0 0 32%;
      height: 200rpx;
      overflow: hidden;

      image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
</style>
