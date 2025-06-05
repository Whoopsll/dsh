<template>
  <view class="index">
    <SafeAreaWrapper title="首页" :pageScroll='false'>
      <view ref="headRef" class="head">
        <CustomNavbar/>
        <Swiper :swiperList="indexData.swiper" />
        <view class="refresh_row">
          <text>你可能感兴趣的书籍</text>
          <view class="right" @click="queryForumList">
            <text>换一批</text>
            <image src="@/static/index/icon_refresh.png"></image>
          </view>
        </view>
      </view>
      <scroll-view class="scroll" scroll-y :style="{height: indexData.headHeight + 'px'}">
        <view class="scroll-container">
          <ForumItem v-for="(item, index) in indexData.forumList" :key="index" :forumInfo="item" />
          <navigator :url="'/pages/forum/allForum'" v-if="indexData.forumList.length >= 4">
            <view class="more">查看更多</view>
          </navigator>
        </view>
      </scroll-view>
    </SafeAreaWrapper>
  </view>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import Swiper from './components/Swiper.vue'
import CustomNavbar from './components/CustomNavbar.vue'
import ForumItem from '../../components/ForumItem/ForumItem.vue'
import { onShow, onLoad } from '@dcloudio/uni-app'
import { forumPage } from '@/service/forum.js'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
import wx from 'weixin-js-sdk'

/**
 * 定义首页数据对象，包含了公告数组和轮播图数组
 */
const indexData = ref({
  swiper: [
    {
      id: 2,
      cover: 'https://blog-manage.oss-cn-shanghai.aliyuncs.com/img/banner.png',
    },
  ],
  forumList: [],
  headHeight: 200 + 'px',
  page: 1,
})
//配置微信分享自定义内容
const configWxShare = () => {
  // 配置微信
  wx.config({
    debug: false,
    // appId: 'wx0055deef26d951ff',
    appId: 'wx0055deef26d951ff',
    timestamp: '1734593740',
    nonceStr: '23e9ef05f43e5',
    signature: '7552dd772c07802dfe562b35554cc12e2547f4f8',
    jsApiList: [
      'updateAppMessageShareData', // 朋友圈分享
      'updateTimelineShareData', // 微信聊天分享
      // 不需要添加其他分享相关的API
    ],
  })
  // 等待配置完成并确保微信 JS SDK 已经加载完成
  wx.ready(function () {
    // 禁用分享功能
    wx.updateAppMessageShareData({
      title: '', // 不设置分享标题
      desc: '', // 不设置分享描述
      link: '', // 不设置分享链接
      imgUrl: '', // 不设置分享图片
    })

    wx.updateTimelineShareData({
      title: '', // 不设置朋友圈分享标题
      desc: '', // 不设置朋友圈分享描述
      link: '', // 不设置朋友圈分享链接
      imgUrl: '', // 不设置朋友圈分享图片
    })
  })

  wx.error(function (err) {
    console.error('微信配置失败:', err)
  })
}

const queryForumList = async () => {
  if (indexData.value.forumList.length < 5) {
    indexData.value.page = 1
  }
  let keyword = uni.getStorageSync('searchKeyword')
  const params = {
    page: indexData.value.page,
    limit: 5,
    keyword: keyword,
  }
  uni.setStorageSync('searchKeyword', '')
  const { data } = await forumPage(params)
  if (data.list.length > 0) {
    indexData.value.forumList = data.list
    indexData.value.page += 1
  }
}

// 子页面通知更新
const updateDate = () => {
  // indexData.value.page = 1
  // queryForumList()
}

const getViewHeight = async () => {
  const query = uni.createSelectorQuery()
  await nextTick()
  query
    .select('.head')
    .boundingClientRect((rect) => {
      if (rect) {
        var height = rect.height // 获取视图的高度
        // 兼容h5
        //#ifdef H5
        indexData.value.headHeight = document.documentElement.scrollHeight - height - 40
        //#endif
        //#ifdef MP-WEIXIN
        const systemInfo = uni.getSystemInfoSync()
        indexData.value.headHeight = systemInfo.windowHeight - height - 80
        //#endif
      } else {
        console.error('未找到该视图')
      }
    })
    .exec()
}

onShow(() => {
  indexData.value.page = 1
  queryForumList()
})

onLoad(() => {
  getViewHeight()
  configWxShare()
})
</script>

<style lang="scss" scoped>

.head {
  margin-top: 10px;
  margin-left: 10rpx;
  margin-right: 10rpx;
}

.index {
  display: flex;
  height: 100%;
  flex-direction: column;
}


.refresh_row {
  display: flex;
  padding: 25rpx 10rpx;
  align-items: center;
  justify-content: space-between;

  .right {
    display: flex;
    align-items: center;

    text {
      color: #1dc09d;
      font-size: 26rpx;
      margin-right: 10rpx;
    }

    image {
      height: 30rpx;
      width: 26rpx;
      object-fit: cover;
    }
  }
}

.scroll {
  overflow: auto;
  // flex: 1;
  scrollbar-width: 0;
  /* #ifdef MP */

  /* #endif */
  /* #ifdef H5 */
  padding-bottom: 40px;
  /* #endif */
}

.scroll-container {
  display: flex;
  flex-direction: column;
  padding: 10rpx;

  .more {
    margin-bottom: 30rpx;
    margin-top: 20rpx;
    padding: 5rpx 5rpx;
    text-align: right;
    color: #0994fb;
    border-radius: 5rpx;
    display: flex;
    justify-content: center;
    font-size: 26rpx;
  }
}
</style>
