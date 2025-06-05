<template>
  <view class="notice" v-if="noticeList.length != 0">
    <view class="left">
      <uni-icons type="sound-filled" size="20" color="#1296db"></uni-icons>
      <text class="text">公告</text>
    </view>
    <view class="center">
      <swiper vertical autoplay :interval="3000" :duration="1500" circular>
        <swiper-item v-for="item in noticeList" :key="item.id">
          <navigator class="noticeItem"
                    :url="'/pages/notice/detail?id=' + item.id">
            {{ item.title }}
          </navigator>
        </swiper-item>
      </swiper>
    </view>
    <navigator url="/pages/notice/notice" class="right">
      <uni-icons type="right" size="16" color="#333"></uni-icons>
    </navigator>
  </view>
</template>

<script setup>
// 接收父组件传入的数组参数
defineProps({
  noticeList: {
    type: Array,
    default: [],
  },
})
</script>

<style lang="scss" scoped>
.notice {
  height: 80rpx;
  line-height: 80rpx;
  background: #fff;
  border-radius: 80rpx;
  display: flex;
  margin: 20rpx 0;
  .left {
    width: 140rpx;
    display: flex;
    align-items: center;
    justify-content: center;

    .text {
      font-weight: 600;
      font-size: 28rpx;
    }
  }
  .center {
    flex: 1;
    swiper {
      height: 100%;
      .noticeItem {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        height: 100%;
        font-size: 24rpx;
        color: #666;
      }
    }
  }
  .right {
    width: 70rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
