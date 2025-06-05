<template>
  <view class="custom-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
    <!-- 左侧返回按钮 -->
    <image @tap="goBack()" class="back-icon" src="/static/icon_back.png"></image>
    <!-- 标题 -->
    <view class="nav-title">
      <text>{{ title }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from "vue";

const title = ref("自定义导航栏");
const statusBarHeight = ref(20); // 状态栏高度

// 组件挂载时获取系统状态栏高度
onMounted(() => {
  const systemInfo = uni.getSystemInfoSync();
  statusBarHeight.value = systemInfo.statusBarHeight || 20;
});

// 返回上一页
const goBack = () => {
  console.log("返沪")
  uni.navigateBack({
    delta: 1, // 返回上一页
  });
};
</script>

<style>
/* 自定义导航栏样式 */
.custom-navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 44px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  margin-bottom: 50rpx;
  padding: 0 10px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
  z-index: 999;
}

.back-icon {
  width: 24px;
  height: 22px;
  z-index: 100;
}

/* 标题 */
.nav-title {
  flex: 1;
  text-align: center;
  padding-right: 40px;
  font-size: 18px;
  font-weight: bold;
}
</style>
