<template>
  <view class="carousel" v-if="swiperList.length != 0">
    <swiper autoplay circular :interval="3000" @change="onChange">
      <swiper-item v-for="item in swiperList" :key="item.id">
        <navigator :url="'/pages/notice/detail?id=' + item.id"
                   hover-class="none" class="navigator">
          <image mode="scaleToFill" class="image" :src="item.cover"></image>
        </navigator>
      </swiper-item>
    </swiper>
    <view class="indicator">
      <text
        v-for="(item, index) in swiperList"
        :key="item.id"
        class="dot"
        :class="{ active: index === activeIndex }">
      </text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
// 接收父组件传入的轮播图数组
defineProps({
  swiperList: {
    type: Array,
    default: [],
  },
})

// 当前激活的索引
const activeIndex = ref(0)

// 轮播图切换
const onChange = (ev) => {
  activeIndex.value = ev.detail.current
}
</script>

<style lang="scss" scoped>
.carousel {
  height: 300rpx;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
  .indicator {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 16rpx;
    display: flex;
    justify-content: center;
    /* 轮播图指示点 */
    .dot {
      width: 30rpx;
      height: 6rpx;
      margin: 0 8rpx;
      border-radius: 6rpx;
      background-color: rgba(255, 255, 255, 0.4);
    }
    .active {
      background-color: #fff;
    }
  }
  .navigator,
  .image {
    width: 100%;
    border-radius: 15rpx;
    height: 100%;
  }
}
</style>
