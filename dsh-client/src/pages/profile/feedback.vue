<template>
  <view class="container">
    <SafeAreaWrapper title="联系我们" :childPage="true">
      <view style="padding: 20rpx 20rpx;">
        <uni-section class="mb-10" title="反馈内容" type="line" style="margin-top: 40rpx;">
          <uni-easyinput

            type="textarea"
            v-model="feedbackInfo"
            placeholder="请描述您反馈的问题或提出宝贵意见"
          ></uni-easyinput>
          <view class="image" @click="uploadImage">
            <image
              v-show="hasImg"
              style="width: 200rpx; height: 200rpx"
              mode="scaleToFill"
              :src="imgUrl"
            ></image>
            <uni-icons v-show="!hasImg" type="images" size="60" color="#a9a9a9"></uni-icons>
          </view>
        </uni-section>
        <uni-section title="联系方式" type="line" padding>
          <uni-easyinput
            class="uni-mt-5"
            trim="all"
            v-model="contact"
            placeholder="请输入您的电话或邮箱，方便我们联系您"
          ></uni-easyinput>
        </uni-section>
        <view
          @click="submitFeedback"
          style="
            height: 80rpx;
            text-align: center;
            line-height: 80rpx;
            margin: 30rpx 60rpx;
            background-color: #0998fb;
            color: white;
            border-radius: 10rpx;
          "
          >提交反馈</view
        >
      </view>
    </SafeAreaWrapper>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import { feedback } from '@/service/user'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
const feedbackInfo = ref('')
const hasImg = ref(false)
const imgUrl = ref('')
const contact = ref('')

const uploadImage = () => {
  uni.chooseImage({
    count: 1,
    success: (chooseImageRes) => {
      const tempFilePaths = chooseImageRes.tempFilePaths
      uni.uploadFile({
        url: 'http://8.153.16.66/dsh-client-api/common/upload/img', //仅为示例，非真实的接口地址
        filePath: tempFilePaths[0],
        name: 'file',
        success: (uploadFileRes) => {
          if (uploadFileRes.statusCode === 200) {
            const res = JSON.parse(uploadFileRes.data)
            imgUrl.value = res.data.urlList[0]
            hasImg.value = true
            uni.showToast({ icon: 'success', title: '上传成功' })
          } else {
            uni.showToast({ icon: 'error', title: '出现错误' })
          }
        },
      })
    },
  })
}

const submitFeedback = async () => {
  if (feedbackInfo.value.trim() == '') {
    uni.showToast({ icon: 'none', title: '反馈内容不能为空' })
    return
  }
  if (contact.value.trim() === '') {
    uni.showToast({ icon: 'none', title: '联系方式不能为空' })
    return
  }
  uni.showLoading({ title: '提交中...' })
  var dto = {
    feedback: feedbackInfo.value,
    contactInfo: contact.value,
    img: imgUrl.value,
  }
  const res = await feedback(dto)
  if (res.code !== 0) {
    uni.showToast({
      icon: 'none',
      title: res.msg,
    })
  } else {
    uni.showToast({
      icon: 'none',
      title: '提交成功，我们会尽快处理',
    })
    imgUrl.value = ''
    hasImg.value = false
    feedbackInfo.value = ''
    contact.value = ''
  }
}
</script>

<style scoped>
page {
  background-color: #f9f9f9;
}
.image {
  margin: 10rpx 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200rpx;
  width: 200rpx;
  border: 1rpx dashed #a9a9a9;
}
</style>
