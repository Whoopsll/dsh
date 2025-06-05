<template>
  <!-- #ifdef MP -->
  <view class="wrapper" :style="wrapperStyle">
    <!-- 自定义导航栏 -->
    <view class="custom-navbar" :class="childPage ? 'box-shadow' : ''" :style="{ paddingTop: navBarStatusheight}">
      <view class="nav-left" @tap="goBack" v-if="childPage">
        <image class="back-icon" src="/static/icon_back.png"></image>
      </view>
      <view class="nav-title">
        <text>{{ title }}</text>
      </view>
    </view>

    <scroll-view :scroll-y="pageScroll" class="content" :enhanced="true"
             :bounces="false" :show-scrollbar="false">
          <!-- 插入父组件的内容 -->
          <slot></slot>
    </scroll-view>

  </view>
  <!-- #endif -->
  <!-- #ifdef H5 -->
 <view class="wrapper">
     <scroll-view :scroll-y="pageScroll" class="content" :enhanced="true"
              :bounces="false" :show-scrollbar="false">

           <slot></slot>
     </scroll-view>
  </view>
  <!-- #endif -->
</template>

<script setup>
import { defineProps, computed } from 'vue'
import { screenUseStore } from '@/stores/screen'

const screenStore = screenUseStore();

// 组件 props
const props = defineProps({
  title: {
    type: String,
    default: "标题"
  },
  childPage: {
    type: Boolean,
    default: false
  },
  pageScroll: {
    type: Boolean,
    default: true

  }
});
//获取导航栏边距
const navBarStatusheight = computed(() => {
  return screenStore.statusBarHeight + 'px';
})
// 计算导航栏高度并动态设置样式
const wrapperStyle = computed(() => {
  return {
    paddingTop: `${screenStore.statusBarHeight + 44}px`,  // 状态栏 + 导航栏高度
    marginBottom: `${screenStore.safeAreaBottom}px`,      // 底部安全区域
  }
});

// 返回上一页
const goBack = () => {
  uni.navigateBack({
    delta: 1
  });
};
</script>

<style scoped>
/* 最外层容器 */
.wrapper {
  width: calc(100vw );
  min-height: 100vh;
  background-color: #ffffff;
  position: absolute;

}

.content {
  height: 100vh;
  width: calc(100vw );
}

/* 自定义导航栏 */
.custom-navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: calc(100vw - 20px);
  height: 44px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  z-index: 999;
   background-color: #ffffff;
}

.box-shadow {
    box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
}

/* 返回按钮 */
.nav-left {
  width: 40px;
  height: 44px;
  display: flex;
  align-items: center;
  z-index: 100;
  justify-content: center;
}

/* 返回按钮图标 */
.back-icon {
  width: 24px;
  height: 22px;
}

/* 标题 - 绝对居中 */
.nav-title {
  left: 0px;
  position: absolute;
  font-size: 18px;
  font-weight: bold;
  width: 100%;
  display: flex;
  color: #222222;
  justify-content: center;
}
</style>
