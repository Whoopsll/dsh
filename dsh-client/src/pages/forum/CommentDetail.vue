<template>
  <view class="page-info">
    <comment-card :comment="commentDetailInfo" :showReply="false" :clickable="false" @click="reply(commentDetailInfo)"></comment-card>
    <view v-for="(item, index) in commentDetailInfo.replyList" :key="index">
      <comment-reply-item :reply="item" :parentId="commentDetailInfo.id"  @click="reply(item)" />
    </view>
    <view class="reply-bar">
      <!-- <uni-icons type="mic-filled" size="30" color="#bbbbbb" @click="showMic"></uni-icons> -->
      <uni-icons type="mic-filled" size="30" color="#bbbbbb"></uni-icons>
      <uni-easyinput class="input" v-model="commentReplyParams.content" :placeholder="placeholder"></uni-easyinput>
      <uni-icons type="paperplane-filled" size="30" color="#bbbbbb" @click="sendComment"></uni-icons>
    </view>
    <uni-popup ref="showMicFlag" background-color="#fff" @maskClick="closePopup">
      <view class="mic-bar">
        <view>{{ minutes }}:{{ seconds }}</view>
        <view :class="['mic-icon wave danger', { ripple: startMicFlag }]" @click="changeMicStatus">
            <view class="circle"></view>
            <view class="circle"></view>
            <view class="circle"></view>
            <view class="content">
              <uni-icons type="mic" size="40" color="#fff"  v-if="!startMicFlag"></uni-icons>
              <uni-icons type="mic-filled" size="40" color="#fff" v-if="startMicFlag"></uni-icons>
            </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import CommentCard from '../../components/CommentCard/CommentCard.vue'
import { commentDetail, forumCommentAdd } from '@/service/forum.js'
import { onLoad } from '@dcloudio/uni-app'
import CommentReplyItem from './components/CommentReplyItem.vue'
import Recorder from 'recorder-core'
import RecordApp from 'recorder-core/src/app-support/app.js'
import '@/uni_modules/Recorder-UniCore/app-uni-support.js'
import 'recorder-core/src/engine/mp3'
import 'recorder-core/src/engine/mp3-engine'

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})

// var vue3This=getCurrentInstance().proxy;
onLoad(() => {
  getCommentDetail()
  RecordApp.UniRenderjsRegister()
})

const commentDetailInfo = ref([])
const getCommentDetail = async () => {
  const { data } = await commentDetail(props.id)
  commentDetailInfo.value = data
  commentReplyParams.value.forumId = commentDetailInfo.value.forumId
  commentReplyParams.value.parentId = commentDetailInfo.value.id
}

const placeholder = ref('请输入评论')
const commentReplyParams = ref({
  content: '',
  type: 1
})
const reply = (comment) => {
  console.log(comment);
  placeholder.value = '回复' + comment.username
  commentReplyParams.value.parentId = comment.id
}

const sendComment = async () => {
  const res = await forumCommentAdd(commentReplyParams.value)
  console.log(res)
  if (res.code === 0) {
    uni.showToast()
    uni.showLoading()
    await getCommentDetail()
    commentReplyParams.value.content = ''
    commentReplyParams.value.parentId = commentDetailInfo.value.id
    uni.hideLoading()
  } else {
    uni.showToast({
      'icon': 'error',
      'title': res.msg
    })
  }
}
const showMicFlag = ref()
const showMic = () => {
  showMicFlag.value.open('bottom')
}
const startMicFlag = ref(false)
const micTime = ref(0)
const intervalId = ref();
const minutes = computed(() => String(Math.floor(micTime.value / 60)).padStart(2, '0'));
const seconds = computed(() => String(micTime.value % 60).padStart(2, '0'));
const changeMicStatus = () => {
  startMicFlag.value = !startMicFlag.value

  if (startMicFlag.value) {
    // recorderManager.start()
    startTimer()
  } else {
    // recorderManager.stop()
    stopTimer()
  }
}
const startTimer = () => {
  if (!intervalId.value) {
    intervalId.value = setInterval(() => {
      micTime.value++;
    }, 1000);
  }
}
const stopTimer = () => {
  clearInterval(intervalId.value);
  intervalId.value = null;
}
const resetTimer = () => {
  stopTimer();
  micTime.value = 0;
}
const closePopup = () => {
  resetTimer()
  startMicFlag.value = false
}
</script>

<style lang="scss" scoped>
.page-info {
  padding-bottom: 80rpx;
}
.reply-bar {
  display: flex;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  justify-content: space-around;
  align-items: center;
  height: 50rpx;
  border-top: 1rpx solid #c8c8c8;
  background-color: #fff;

  .btn {
    text-align: center;
    height: 60rpx;
  }
}

.mic-bar {
  height: 300rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20rpx;

  .mic-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 100%;
    height: 150rpx;
    width: 150rpx;
    background-color: rgb(21, 79, 227);
  }
}


    .wave {
			position: relative;
		  font-size: 28px;
		}

		.wave .circle {
		    position: absolute;
		    border-radius: 50%;
		    opacity: 0;
        width: 300rpx;
        height: 300rpx;
		}

		.wave.ripple .circle {
		    width: 200%;
    		height: 200%;
		    border: 3px solid #fff;
		}

		.wave.ripple .circle:first-child {
			animation: circle-opacity 2s infinite;
		}

		.wave.ripple .circle:nth-child(2) {
			animation: circle-opacity 2s infinite;
			animation-delay: .3s;
		}

		.wave.ripple .circle:nth-child(3) {
		 	animation: circle-opacity 2s infinite;
			animation-delay: .6s;
		}

		.wave.ripple.danger {
		    color: rgb(34, 159, 255);
		}

		.wave.ripple.danger .circle {
			border-color: rgb(34, 159, 255);
		}

		@keyframes circle-opacity{
		    from {
		        opacity: 1;
		        transform: scale(0);
		    }
		    to {
		        opacity: 0;
		        transform: scale(1);
		    }
		}
</style>
