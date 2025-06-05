<template>
  <view class="box">
    <SafeAreaWrapper title="关注">
      <view class="top-bar">
        <view>关注的读书会</view>
        <view>{{ followForumList.length || 0 }} 个</view>
      </view>
      <view class="follow-body">
        <view v-if="followForumList.length > 0">
          <scroll-view scroll-y>
            <forum-item v-for="(item, index) in followForumList" :key="index" :forum-info="item" />
          </scroll-view>
        </view>
        <view v-else class="none">
          <text>微信扫码</text>
          <text>关注更多读书汇</text>
        </view>
      </view>
    </SafeAreaWrapper>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import ForumItem from '../../components/ForumItem/ForumItem.vue';
import { followedPage } from '@/service/forum'
import { onShow } from '@dcloudio/uni-app'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
const followForumList = ref([])

const getFollowList = async () => {
  const { data } = await followedPage({
    page: 1,
    limit: 9
  })
  followForumList.value = data.list
}

onShow(() => {
  getFollowList()
})
</script>

<style lang="scss" scoped>
page {

}
.box {

}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  color: #000;
  font-size: 28rpx;
  padding: 20rpx 50rpx;
  border-bottom: 1rpx solid #ddd;
}

.follow-body {
  padding: 20rpx;
  /* #ifdef MP */
  padding-bottom: 100px;
  /* #endif */
  /* #ifdef H5 */
  padding-bottom: 50px;
  /* #endif */
  .none {
    display: flex;
    flex-direction: column;
    padding: 5rpx;
    margin: 0 10rpx;
    align-items: center;
    justify-content: center;
    width: 200rpx;
    height: 300rpx;
    border: 1rpx solid #ddd;
    font-size: 24rpx;
    line-height: 1.5;
    color: #8e8e8e;
  }
}
</style>
