<template>
  <view>
    <SafeAreaWrapper title="书籍信息详情" :childPage="true">
      <view class="card">
        <!-- <view class="top">
          <image class="back" src="@/static/icon-back.png" @click="back()"></image>
          <text class="name">{{forumDetailInfo.name}}</text>
          <view class="share">
            <uni-icons type="redo" size="24" color="#8e8e8e" @click="shareLink"></uni-icons>
            <text>分享</text>
          </view>
        </view> -->
        <view style="padding: 0 40rpx">
          <view>
            <view style="position: relative; margin-bottom: 40rpx; margin-top: 40rpx">
              <text class="title">简介</text>
              <view class="line"></view>
            </view>
            <view class="desc">{{ forumDetailInfo.bookIntroduction }}</view>
          </view>
          <view v-if="forumDetailInfo.resourceUrl != ''">
            <view style="position: relative; margin-bottom: 20rpx; margin-top: 40rpx">
              <text class="title">资源链接</text>
              <view class="line">
              </view>
            </view>
            <view style="margin-bottom: 40rpx;">
              <view v-if="!isFollow">
                <text style="display: block;">****************</text>
                <text style="display: block;font-size: 12px; color: red;">*关注作者即可查看链接内容</text>
              </view>
              <view v-else style="display: flex;">
                <div  class="overflow-text">{{forumDetailInfo.resourceUrl}}</div>
                <text style="color: #1dc09d;" @click="copyText()">【复制链接】</text>
              </view>
            </view>
          </view>
          <view>
            <view style="position: relative; margin-bottom: 40rpx">
              <text class="title">版权</text>
              <view class="line"></view>
            </view>

            <view style="display: flex">
              <view class="left">
                <text class="label">作者</text>
                <text class="label">出版社</text>
                <text class="label">出版时间</text>
                <text class="label">ISBN</text>
                <!-- <text class="label">字数</text> -->
                <text class="label">电子书价格</text>
                <text class="label">分类</text>
              </view>
              <view class="right">
                <text class="label" style="color: #1dc09d">{{ forumDetailInfo.writer }}</text>
                <text class="label" style="color: #1dc09d">{{ forumDetailInfo.publisherName }}</text>
                <text class="label">{{ forumDetailInfo.publisherTime }}</text>
                <text class="label">{{ forumDetailInfo.isbn }}</text>
                <!-- <text class="label">{{ getFontCount() }}</text> -->
                <text class="label">{{ forumDetailInfo.price }}元</text>
                <text class="label">{{ forumDetailInfo.category }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </SafeAreaWrapper>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import { forumDetail } from '@/service/forum.js'
import TextExpand from '../../components/TextExpand/TextExpand.vue'
import { onLoad } from '@dcloudio/uni-app'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})
const isFollow = ref(false)
onLoad(() => {
  getDetailInfo()
})

const back = () => {
  uni.navigateBack()
}
const copyText = () => {
  uni.setClipboardData({
    data: forumDetailInfo.value.resourceUrl,
    success: () => {
      uni.showToast({ title: '链接复制成功', icon: 'success' });
    },
    fail: () => {
      uni.showToast({ title: '复制失败', icon: 'error' });
    }
  });
};
const forumDetailInfo = ref()
const getDetailInfo = async () => {
  var param = {
    forumId: props.id,
  }
  const { data } = await forumDetail(param)
  forumDetailInfo.value = data
  isFollow.value = data.follow
  uni.setNavigationBarTitle({ title: forumDetailInfo.value.name })
}
// const getFontCount = () => {
//   // 生成 200000 到 500000 的随机数
//   const randomNumber = Math.floor(Math.random() * (200000 - 200000 + 1)) + 200000

//   // 格式化为 "千" 字的形式，例如 250000 => "25万"
//   const formattedNumber = (randomNumber / 1000).toFixed(0) + '千字'

//   return formattedNumber
// }
const shareLink = () => {
  uni.setClipboardData({
    data: window.location.href,
    success: () => {
      uni.showToast({ icon: 'none', title: '复制链接成功' })
    },
  })
}
</script>

<style lang="scss" scoped>
page {
  background-color: #fff;
}
.card {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 10rpx;

  .top {
    display: flex;
    height: 80rpx;
    border-bottom: 1rpx solid #f2f2f5;
    position: relative;
    align-items: center;

    .back {
      height: 48rpx;
      width: 44rpx;
      position: absolute;
      left: 20rpx;
    }

    .name {
      font-size: 38rpx;
      font-weight: bold;
      text-align: center;
      width: 100%;
    }

    .share {
      position: absolute;
      right: 20rpx;
      display: flex;
      font-size: 26rpx;
      align-items: center;
    }
  }

  .title {
    font-size: 38rpx;
    font-weight: bold;
  }

  .line {
    position: absolute;
    bottom: 5rpx;
    width: 90rpx;
    height: 10rpx;
    background-color: rgba(24, 190, 154, 0.7);
  }

  .left {
    width: 200rpx;
    display: flex;
    flex-direction: column;
  }

  .right {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  .label {
    font-size: 26rpx;
    margin-bottom: 15rpx;
    flood-color: #828282;
  }

  .desc {
    color: #666666;
    font-size: 28rpx;
    line-height: 1.5;
    padding-bottom: 30rpx;
    border-bottom: 1rpx solid #f2f2f5;
    margin-bottom: 30rpx;
  }
}
.overflow-text {
  width: 450rpx;overflow: hidden; white-space: break-spaces; text-overflow: ellipsis;
}
</style>
