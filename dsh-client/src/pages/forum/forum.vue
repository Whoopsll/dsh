<template>
  <view class="container">
    <SafeAreaWrapper title="论坛" :childPage="true">
      <view class="card">
        <view
          v-if="type == 1"
          @click="goHome()"
          style="margin: 10rpx 10rpx; display: flex; align-items: center"
        >
          <image src="@/static/icon-back.png" style="width: 36rpx; height: 48rpx"></image>
          <!-- #ifdef H5 -->
          <text class="back-text">返回</text>
          <!-- #endif -->
        </view>
        <view class="top" style="position: relative">
          <image class="cover" :src="getImagePath(forumDetailInfo.cover)"></image>
          <view class="info" @click="toForumDetail">
            <view class="bookName">{{ forumDetailInfo.bookName }}</view>
            <view style="display: flex; align-items: center">
              <text class="author">{{ forumDetailInfo.writer }}</text>
              <text class="tag">作者</text>
            </view>
            <view class="description">{{ forumDetailInfo.bookIntroduction }}</view>
            <view class="more" v-if="forumDetailInfo.bookIntroduction != null">
              <text v-if="forumDetailInfo.bookIntroduction.length > 60">更多</text>
            </view>
          </view>
          <view style="position: absolute; right: 270rpx; bottom: 40rpx" v-if="redirectUrl != ''"
            ><a :href="redirectUrl">点此获取资源</a></view
          >
        </view>
        <view class="count-data">
          <view style="flex: 1">
            <view class="data">
              <text class="count">{{ getLikeCount(forumDetailInfo) }}</text>
              <text class="unit">人</text>
            </view>
            <view class="label">点赞</view>
          </view>
          <view style="width: 1px; margin: 15rpx 0; background-color: #f2f4f6"></view>
          <view style="flex: 1">
            <view class="data">
              <text class="count">{{
                forumDetailInfo == null ? 0 : forumDetailInfo.followCount
              }}</text>
              <text class="unit">人</text>
            </view>
            <view class="label">关注</view>
          </view>
          <view style="width: 1px; margin: 15rpx 0; background-color: #f2f4f6"></view>
          <view style="flex: 1">
            <view class="data">
              <text class="count">{{ commentLists.length }}</text>
              <text class="unit">条</text>
            </view>
            <view class="label">评论</view>
          </view>
        </view>
      </view>

      <view class="comment-card">
        <text class="title">交流区</text>
        <CommentCard
          v-for="(item, index) in commentLists"
          @handleComment="handleComment"
          :key="index"
          :comment="item"
        />
      </view>

      <view class="like-card">
        <view class="can-like">
          <text>猜你喜欢</text>
          <view class="right" @click="queryForumList">
            <text>换一批</text>
            <image src="@/static/index/icon_refresh.png"></image>
          </view>
        </view>
        <scroll-view class="scroll" scroll-x v-if="likeData.length > 0">
          <view class="scroll-container">
            <view
              v-for="(item, index) in likeData"
              :key="index"
              @click="refreshPage(item.id)"
              style="margin-right: 15rpx"
            >
              <image class="cover-img" :src="getImagePath(item.cover)"></image>
              <text style="font-size: 26rpx; display: block">{{ item.name }}</text>
            </view>
          </view>
        </scroll-view>
      </view>
      <view class="button">
        <text class="commont-btn" @click="showComment()">评论</text>
        <text v-if="forumDetailInfo != null" class="follow-btn" @click="followAction">{{
          forumDetailInfo.follow ? '取消关注' : '关注'
        }}</text>
      </view>
    </SafeAreaWrapper>

    <uni-popup ref="commentPop" type="bottom" background-color="#fff">
      <view class="commentPop">
        <uni-easyinput type="textarea" placeholder="请输入评论" v-model="commentContent" />
        <view class="flex justify-start items-center bg-red-50 h-5">
          <image
            v-for="(item, index) in commentImgList"
            :key="index"
            :src="item"
            class="comment-img"
          />
        </view>
        <view style="display: flex; justify-content: space-between; padding: 10rpx">
          <uni-icons type="image" size="30" @click="onCommentImg"></uni-icons>
          <uni-icons type="paperplane-filled" size="30" @click="sendComment"></uni-icons>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import TextExpand from '../../components/TextExpand/TextExpand.vue'
import CommentCard from '../../components/CommentCard/CommentCard.vue'
import ForumItem from '../../components/ForumItem/ForumItem.vue'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'

import {
  forumDetail,
  commentList,
  forumFollowed,
  forumCommentAdd,
  getLikedBook,
} from '@/service/forum.js'
import { onLoad, onShow } from '@dcloudio/uni-app'
const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})

const type = ref(0)
const redirectUrl = ref('')

const fabPattern = {
  icon: 'chat-filled',
}

const commentLists = ref([])
const getLikeCount = (forumDetailInfo) => {
  return forumDetailInfo == null
    ? 0
    : forumDetailInfo.id == 22 ||
      forumDetailInfo.id == 23 ||
      forumDetailInfo.id == 24 ||
      forumDetailInfo.id == 25
    ? forumDetailInfo.id * 10 + 6
    : commentLists.value.length
}
onLoad((e) => {
  type.value = e.type
  if (type.value == 1) {
    //扫码进入的书籍页面,将id保存到本地
    uni.setStorageSync('forumId', e.id)
  }
  if (e.id) {
    props.id = e.id
  }
  // else {
  //   uni.setStorageSync('tempForumId', props.id)
  // }
  if (e.redirectUrl) {
    // 获取资源重定向链接
    let currentUrl = window.location.href
    let str = 'redirectUrl='
    redirectUrl.value = currentUrl.slice(currentUrl.indexOf(str) + str.length)
  }
  // 获取
  getDetailInfo()
  getCommentList()
})
const forumDetailInfo = ref()

const likeData = ref([])
const commentId = ref(null)

const goHome = () => {
  uni.reLaunch({
    url: '/pages/index/index',
  })
}

const getDetailInfo = async () => {
  var param = {
    forumId: props.id,
    type: type.value,
  }
  const { data } = await forumDetail(param)
  forumDetailInfo.value = data
  if (uni.getStorageSync('forumId')) {
    type.value = 1
    uni.removeStorageSync('forumId')
  }
  if (uni.getStorageSync('tempForumId')) {
    uni.removeStorageSync('tempForumId')
  }
}

const toForumDetail = () => {
  uni.navigateTo({
    url: '/pages/forum/forumDetail?id=' + props.id,
  })
}

const refreshPage = (id) => {
  uni.reLaunch({
    url: '/pages/forum/forum?id=' + id,
  })
}

const getCommentList = async () => {
  const { data } = await commentList(props.id)
  commentLists.value = data
  queryForumList()
}
const showMoreFlag = ref(true)
// 计算属性，默认显示前3条评论，点击查看更多时显示全部评论
const displayedComments = computed(() => {
  return showMoreFlag.value ? commentLists.value.slice(0, 3) : commentLists.value
})
const showMore = () => {
  showMoreFlag.value = !showMoreFlag.value
}

const followAction = async () => {
  const { code } = await forumFollowed(props.id)
  if (code === 0) {
    getDetailInfo()
    if (!forumDetailInfo.value.follow) {
      uni.showToast({ duration: 300, title: '关注成功' })
    } else {
      uni.showToast({ duration: 300, title: '取关成功' })
    }
    const pages = getCurrentPages()
    const prevPage = pages[pages.length - 2] //获取上一页
    if (prevPage && prevPage.updateDate) {
      prevPage.updateDate()
    }
  } else {
    uni.showToast({ icon: 'error', duration: 300 })
  }
}

const commentPop = ref()
const showComment = (id) => {
  commentId.value = id
  commentPop.value.open('bottom')
}
const commentContent = ref()
const sendComment = async () => {
  const commentDto = {
    forumId: props.id,
    content: commentContent.value,
    type: 1,
    fileType: 0,
    files: commentImgList.value,
  }
  if (commentId.value) {
    commentDto.parentId = commentId.value
  }
  const res = await forumCommentAdd(commentDto)
  if (res.code === 0) {
    uni.showToast()
    uni.showLoading()
    await getCommentList()
    commentContent.value = ''
    uni.hideLoading()
    commentId.value = null
  } else {
    uni.showToast({
      icon: 'error',
      title: res.msg,
    })
    commentId.value = null
  }
  commentPop.value.close()
}

const queryForumList = async () => {
  const params = {
    type: forumDetailInfo.value.categoryId,
    bookId: forumDetailInfo.value.bookId,
  }
  const { data } = await getLikedBook(params)
  if (data.length > 0) {
    likeData.value = data
  }
}

const handleComment = (data) => {
  showComment(data)
}

const commentImgList = ref([])
const onCommentImg = async () => {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      console.log(res)
      // 文件路径
      const tempFiles = res.tempFilePaths
      // 上传
      uploadFile(tempFiles[0]).then((fileRes) => {
        console.log(fileRes)
      })
    },
  })
}

const uploadFile = (file) => {
  // 文件上传
  uni.uploadFile({
    url: 'http://8.153.16.66/dsh-client-api/common/upload/img',
    name: 'file', // 后端数据字段名
    filePath: file,
    success: (res) => {
      // 判断状态码是否上传成功
      if (res.statusCode === 200) {
        commentImgList.value = commentImgList.value.concat(JSON.parse(res.data).data.urlList)
        uni.showToast({ icon: 'success', title: '上传成功' })
      } else {
        uni.showToast({ icon: 'error', title: '出现错误' })
      }
    },
  })
}
const getImagePath = (cover) => {
  var name = 'default_cover.png'
  if (cover == null) {
    return `/static/${name}`
  }
  return cover
}

onShow(() => {})
</script>

<style lang="scss" scoped>

.card {
  display: flex;
  flex-direction: column;
  padding: 10rpx 10rpx;
  border-radius: 10rpx;

  .top {
    display: flex;
    padding: 20rpx 10rpx;
    border-bottom: 1rpx solid #f2f4f6;

    .cover {
      width: 240rpx;
      height: 280rpx;
      margin: 0 10rpx 10rpx 10rpx;
      border-radius: 5rpx;
    }

    .info {
      flex: 1;
      padding: 5rpx 10rpx;

      .bookName {
        font-size: 36rpx;
        font-weight: 700;
        width: 90%;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        margin-bottom: 15rpx;
        text-overflow: ellipsis;
      }

      .author {
        font-size: 26rpx;
        color: #1dc09d;
      }

      .tag {
        background-color: #1dc09d;
        border-radius: 5rpx;
        padding: 2rpx 5rpx;
        margin-left: 15rpx;
        font-size: 22rpx;
        color: white;
      }

      .description {
        max-width: 420rpx;
        white-space: wrap;
        overflow: hidden;
        margin-top: 20rpx;
        color: #696969;
        font-size: 26rpx;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        display: -webkit-box;
        -webkit-box-orient: vertical;
      }

      .more {
        width: 100%;
        text-align: right;
        font-size: 26rpx;
        color: #1dc09d;
        margin-top: 20rpx;
      }
    }
  }
}

.count-data {
  display: flex;
  justify-content: space-between;
  margin-top: 30rpx;
  margin-bottom: 20rpx;
  padding: 10rpx 0;

  .data {
    display: flex;
    justify-content: center;
    flex: 1;
    align-items: bottom;

    .count {
      font-size: 64rpx;
    }

    .unit {
      color: #696969;
      padding-top: 38rpx;
      margin-left: 5rpx;
      font-size: 24rpx;
    }
  }

  .label {
    font-size: 24rpx;
    text-align: center;
    color: #696969;
  }
}

.comment-card {
  margin-top: 20rpx;
  display: flex;
  flex-direction: column;
  padding: 30rpx 20rpx;
  background-color: white;

  .title {
    flex: 1;
    font-size: 32rpx;
    color: #5f5f5f;
    padding-bottom: 30rpx;
    padding-left: 20rpx;
    border-bottom: 1rpx solid #f2f2f5;
  }
}

.button {
  display: flex;
  position: fixed;
  bottom: 0rpx;
  height: 120rpx;
  width: 100%;
  background-color: white;

  .commont-btn {
    flex: 1;
    border: 1px solid #f2f4f6;
    font-size: 26rpx;
    color: #ababab;
    margin: 15rpx 10rpx 15rpx 20rpx;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 10rpx;
  }

  .follow-btn {
    flex: 1;
    background-color: #1dc09d;
    color: white;
    text-align: center;
    border-radius: 10rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 15rpx 20rpx 15rpx 10rpx;
    font-size: 26rpx;
  }
}

.commentPop {
  padding: 20rpx;
}

.comment-img {
  height: 100rpx;
  width: 100rpx;
  border: 1rpx solid #bcbcbc;
  margin: 10rpx;
}

.like-card {
  display: flex;
  flex-direction: column;
  margin-top: 30rpx;
  /* #ifdef MP */
  padding: 20rpx 20rpx 80px 20rpx;
  /* #endif */
  /* #ifdef H5 */
  padding: 20rpx 20rpx;
  /* #endif */
  background-color: #fff;

  .can-like {
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

  .scroll-container {
    display: flex;
    margin-bottom: 130rpx;

    .cover-img {
      height: 260rpx;
      border-radius: 5rpx;
      width: 220rpx;
    }
  }
}
</style>
