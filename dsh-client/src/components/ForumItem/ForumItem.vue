<template>
  <navigator
    :url="'/pages/forum/forum?id=' + forumInfo.id"
    class="scroll-item"
    hover-class="none"
    :key="forumInfo.id"
  >
    <view class="card-item">
      <view class="card-content">
        <image :src="forumInfo.cover" class="cover-img"></image>
        <view class="card-right">
          <image class="icon-follow" :src="getImagePath(forumInfo.follow)"></image>
          <text class="label">累计{{ forumInfo.followCount }}人推荐</text>
          <text class="title">{{ forumInfo.name }}</text>
          <text class="author">{{ forumInfo.author }}</text>
          <view class="description">{{ forumInfo.bookDescription }}</view>
        </view>
      </view>
      <view class="tags" v-if="isShowTag">
        <text class="card-tag label" v-if="forumInfo.type != null">{{ forumInfo.type }}</text>
        <text class="card-tag label">1000人读过</text>
        <text class="card-tag label" v-if="forumInfo.commentCount"
          >{{ forumInfo.commentCount }}条优质回答</text
        >
      </view>
    </view>
  </navigator>
</template>

<script setup>
import { ref, computed } from 'vue'
const prop = defineProps({
  forumInfo: {
    type: Object,
    default: {},
  },
  isShowTag: {
    type: Boolean,
    default: true,
  },
})

const selectImage = ref('icon_follow_selected.png')
const unSelectImage = ref('icon_follow_unselect.png')

const getImagePath = (follow) => {
  if (follow) {
    return `/static/tabs/${selectImage.value}`
  }
  return `/static/tabs/${unSelectImage.value}`
}
</script>

<style lang="scss" scoped>
.card-item {
  display: flex;
  flex-direction: column;
  padding: 5rpx;
  margin: 10rpx;
  align-items: flex-start;
  justify-content: center;
  margin-top: 20rpx;

  .card-content {
    display: flex;
    width: 100%;
    flex-direction: row;

    .cover-img {
      margin: 5rpx;
      height: 260rpx;
      border-radius: 5rpx;
      width: 220rpx;
    }

    .card-right {
      flex: 1;
      display: flex;
      position: relative;
      flex-direction: column;
      align-items: flex-start;
      margin-left: 20rpx;

      .icon-follow {
        position: absolute;
        right: 20rpx;
        top: 10rpx;
        height: 50rpx;
        width: 50rpx;
      }

      .title {
        font-size: 32rpx;
        color: #2f2e35;
        margin: 10rpx 0;
        font-weight: bold;
      }

      .author {
        font-size: 22rpx;
        color: #2f2e35;
      }

      .description {
        color: #b5b5b5;
        font-size: 26rpx;
        margin-top: 10rpx;
        max-width: 420rpx;
        white-space: wrap;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        display: -webkit-box;
        -webkit-box-orient: vertical;
      }
    }
  }

  .tags {
    display: flex;
    margin-top: 20rpx;

    .card-tag {
      border-radius: 20rpx;
      background-color: #e4fbf6;
      padding: 5rpx 15rpx;
      margin-right: 15rpx;
    }
  }

  .label {
    font-size: 22rpx;
    color: #2bc5a4;
  }
}
</style>
