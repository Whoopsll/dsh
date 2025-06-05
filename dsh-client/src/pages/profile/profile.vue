<template>
  <view class="my">
    <view style="width: 100vw; position: relative; margin-bottom: 120rpx">
      <image
        src="../../static/bg.png"
        style="position: absolute; height: 300rpx; top: 0; width: 100%; z-index: -1"
        mode="aspectFill"
      ></image>
      <view class="userInfo">
        <view class="left">
          <image
            :src="
              wuser?.avatar ||
              'https://img.ixintu.com/download/jpg/20201130/a5a797801c5e0abd58d80d2182081608_512_512.jpg!ys'
            "
            style="width: 100%; height: 100%"
          />
        </view>
        <view class="center">
          <view class="name" v-if="wuser?.nickname || wuser?.wxOpenId">{{ wuser.nickname }}</view>
          <view class="noLogin" v-else>暂未登录</view>
        </view>
      </view>
    </view>
    <view class="item-menu">
      <uni-grid
        class="item-bar"
        v-if="wuser"
        :column="3"
        :highlight="false"
        :showBorder="false"
        :square="false"
      >
        <uni-grid-item
          v-for="(item, index) in itemBarList"
          :index="index"
          :key="index"
          @tap="jumpToPage(item.type)"
        >
          <view class="grid-item-box">
            <!-- <uni-icons :type="item.icon" :size="30" color="#777" /> -->
            <image style="height: 24px; width: 24px" :src="getMyIcon(item.icon)" />
            <text class="text">{{ item.title }}</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </view>

    <view class="action">
      <view class="button" v-if="!wuser" @click="handleClickLogin">前往登录</view>
      <view class="button" v-else @click="logoutAction">退出登录</view>
    </view>
    <!-- <SafeAreaWrapper title="我的"> -->

    <!-- 操作 -->
    <!-- <view class="action-info">
        <navigator class="row" url="/pages/profile/question">
          <view class="row">
            <view class="left">
              <uni-icons class="icon" type="notification" size="26" color="#1296db"></uni-icons>
              <text class="txt">问答</text>
            </view>
            <view class="right">
              <uni-icons class="icon" type="right" size="20" color="#b9b9b9"></uni-icons>
            </view>
          </view>
        </navigator>
        <navigator class="row" url="/pages/profile/feedback">
          <view class="row">
            <view class="left">
              <uni-icons class="icon" type="info" size="26" color="#1296db"></uni-icons>
              <text class="txt">联系我们</text>
            </view>
            <view class="right">
              <uni-icons class="icon" type="right" size="20" color="#b9b9b9"></uni-icons>
            </view>
          </view>
        </navigator>
      </view> -->
    <!-- 登录 -->
    <!-- 用户信息 -->

    <!-- </SafeAreaWrapper> -->
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
import { onLoad } from '@dcloudio/uni-app'
import { queryUserInfo } from '@/service/user'
import { logout } from '@/service/auth'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
// item-bar数据
const itemBarList = [
  {
    icon: 'icon_my_comment.png',
    type: 1,
    title: '我的评论',
    color: '',
    url: '',
  },
  {
    icon: 'icon_my_star.png',
    type: 2,
    title: '我的收藏',
    color: '',
    url: '',
  },
  {
    icon: 'icon_my_reply.png',
    type: 3,
    title: '我的回复',
    color: '',
    url: '',
  },
  // {
  //   icon: 'icon_my_forum.png',
  //   type: 4,
  //   title: '我的论坛',
  //   color: '',
  //   url: '',
  // },
  {
    icon: 'icon_my_answer.png',
    type: 5,
    title: '我的问答',
    color: '',
    url: '',
  },
  {
    icon: 'icon_my_contact.png',
    type: 6,
    title: '联系我们',
    color: '',
    url: '',
  },
]

onLoad(() => {
  getUserInfo()
})
const wuser = ref()
const getUserInfo = async () => {
  const res = await queryUserInfo()
  if (res.code !== 0) {
    uni.showToast({
      icon: 'none',
      title: res.msg,
    })
  }
  wuser.value = res.data
}

// 前往登录
const handleClickLogin = () => {
  uni.navigateTo({
    url: '/pages/login/login',
  })
}

const getMyIcon = (name) => {
  return `/static/mine/${name}`
}

const logoutAction = async () => {
  const res = await logout()
  if (res.code === 0) {
    uni.showToast({ icon: 'none', title: '退出成功' })
    wuser.value = null
    userStore.clearUserInfo()
    uni.clearStorage()
  }
}

const jumpToPage = (type) => {
  switch (type) {
    case 1:
    case 2:
    case 3:
      uni.navigateTo({
        url: '/pages/profile/userComment?commentType=' + type,
      })
      break
    case 4:
      break
    case 5:
      uni.navigateTo({
        url: '/pages/profile/question',
      })
      break
    case 6:
      uni.navigateTo({
        url: '/pages/profile/feedback',
      })
      break
  }
}
</script>

<style lang="scss" scoped>
page {
  overscroll-behavior: contain;
}

.my {
  height: calc(100vh);

  .userInfo {
    display: flex;
    align-items: center;
    padding: 120rpx 30rpx 0 30rpx;
    border-radius: 20rpx;
    color: #fff;
    .left {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 30rpx;
    }
    .center {
      flex: 1;
      .name {
        font-size: 36rpx;
        margin-bottom: 10rpx;
      }
      .phone {
        font-size: 28rpx;
      }
    }
  }

  .score {
    padding: 30rpx;
    background: #f9e5cb;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom-left-radius: 20rpx;
    border-bottom-right-radius: 20rpx;

    .left {
      display: flex;
      align-items: center;
      color: #946c37;
      .text {
        margin-left: 18rpx;
      }
    }
    .right {
      border: 1px solid #946c37;
      border-radius: 20rpx;
      padding: 6rpx 10rpx;
      font-size: 24rpx;
      color: #946c37;
    }
  }

  .row {
    display: flex;
    align-items: center;
    padding: 20rpx 30rpx;
    justify-content: space-between;
    border-bottom: 1px solid #ccc;
    .left {
      display: flex;
      align-items: center;
      flex: 1;
      .txt {
        font-size: 28rpx;
        margin-left: 20rpx;
      }
    }
    .right {
    }
    // 最后一个子元素没有下边框
    &:last-child {
      border-bottom: none;
    }
  }

  .action-info {
    background: #fff;
    margin-top: 30rpx;
    border-radius: 20rpx;
  }

  .action {
    text-align: center;
    line-height: 90rpx;
    margin: 80rpx 20rpx 20rpx 20rpx;
    font-size: 32rpx;
    color: #fff;
    .button {
      background-color: #34d22a;
      margin-bottom: 20rpx;
      border-radius: 20rpx;
    }
  }
}

.item-menu {
  margin: 10rpx 20rpx 40rpx 20rpx;
  border-radius: 10rpx;
  box-shadow: 0 0px 8px rgba(0, 0, 0, 0.2);
}

.item-bar {
  margin: 20px 0;
  border-radius: 10rpx;

  .grid-item-box {
    flex: 1;
    // position: relative;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 25rpx 0;

    .text {
      font-size: 24rpx;
      margin-top: 20rpx;
    }
  }
}
</style>
