<template>
  <view>
    <SafeAreaWrapper title="书籍列表" :childPage="true">
      <scroll-view
        enable-back-to-top
        refresher-enabled
        @refresherrefresh="onRefresherrefresh"
        :refresher-triggered="isTriggered"
        @scrolltolower="onScrolltolower"
        class="scroll-view"
        scroll-y
      >
        <view class="container">
          <ForumItem style="width: 100%;" v-for="(item, index) in forumList" :key="index" :forumInfo="item" />
        </view>
      </scroll-view>
    </SafeAreaWrapper>

</view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { forumPage } from '@/service/forum.js'
import ForumItem from '../../components/ForumItem/ForumItem.vue'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
onMounted(() => {
  queryForumList()
})
const params = ref({
  page: 1,
  limit: 15
})
const totalNum = ref()
const forumList = ref()
const queryForumList = async () => {
  const { data } = await forumPage(params.value)
  if (params.value.page === 1) {
    forumList.value = data.list
  } else {
    forumList.value = forumList.value.concat(data.list)
    console.log(forumList.value);
  }

  totalNum.value = data.total
}

const onRefresherrefresh = async () => {
  console.log('refresh');
  isTriggered.value = true
  params.value.page = 1
  await queryForumList()
  isTriggered.value = false
}

// 是否触发下拉刷新
const isTriggered = ref(false)

// 滚动到底部
const onScrolltolower = () => {
  console.log('bottom');
  params.value.page++;
  if (forumList.value.length < totalNum.value) {
    queryForumList()
  } else {
    uni.showToast({
      icon: 'error',
      duration: 300,
      title: '没有更多书籍！'
    })
  }
}
</script>

<style scoped>
page{
  background-color: #f7f7f7;
  height: 100%;
  overflow: hidden;
}
.scroll-view {
  height: 94vh;
  padding: 0 0 10rpx;
}
.container {
  padding: 20rpx;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  min-height: 700rpx;
}
</style>
