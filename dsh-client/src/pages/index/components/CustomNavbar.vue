<template>
  <view class="navbar">
    <view class="search" @click="handleSearchClick">
      <image class="icon" src="@/static/icon_search.png"/>
      <text class="icon-search">作者/书名/关键词</text>
    </view>
  </view>
</template>

<script setup>
// 获取屏幕边界到安全区域距离
const { safeAreaInsets } = uni.getSystemInfoSync()
// 点击搜索框跳转搜索页面
const handleSearchClick = () => {
  uni.navigateTo({
    url: '/pages/search/search',
  })
}
</script>

<style lang="scss" scoped>
/* 自定义导航条 */
.navbar {
  position: relative;
  display: flex;
  flex-direction: column;
  .logo {
    display: flex;
    align-items: center;
    height: 64rpx;
    padding-left: 30rpx;
    .logo-text {
      flex: 1;
      line-height: 28rpx;
      color: #fff;
      margin: 2rpx 0 0 20rpx;
      padding-left: 20rpx;
      border-left: 1rpx solid #fff;
      font-size: 36rpx;
    }
  }
  .search {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10rpx 0 26rpx;
    height: 64rpx;
    margin: 16rpx 20rpx;
    color: #cdcdcd;
    font-size: 28rpx;
    border-radius: 32rpx;
    display: flex;
    align-items: center;
    background-color: #f1f1f5;
  }
  .icon-search {
     width: 100%;
     margin-left: 20rpx;
  }
  .icon {
    height: 38rpx;
    width: 36rpx;
  }
}
</style>
