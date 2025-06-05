<!-- 初始化用户信息页面 -->
<template>
	<view class="box">
    <SafeAreaWrapper title="获取用户信息" :childPage="true">
    <view class="title">获取用户头像昵称</view>
     <view class="row">
       <button class="avatar-wrapper" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
         <image class="avatar" :src="avatarUrl" mode="aspectFill"/>
       </button>
     </view>
     <view class="row">
        <view class="label">昵称:</view>
        <input type="nickname" v-model="nickname" class="nickname" placeholder="请输入昵称"/>
     </view>
     <view class="submit-btn" @click="submitUserInfo()">保存</view>
     </SafeAreaWrapper>
	</view>
</template>

<script setup>
  import { ref } from 'vue'
  import { initProfile } from '@/service/auth'
  import { uploadFile } from '@/utils/upload.js';
  import { useUserStore } from '@/stores/user'
  import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
  const userStore = useUserStore()
	const avatarUrl = ref('https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0')
  //记录上传后的avatar
  const avatar = ref('')
  const nickname = ref('')
  const onChooseAvatar = (e) => {
    if(!e.default) {
      avatarUrl.value = e.detail.avatarUrl
    }
    //上传文件
    uploadFile(avatarUrl.value).then(res => {
      if(res.code == 0) {
        //上传成功，获取图片链接
        avatar.value = res.data.urlList[0]
      }
    })
  }

  //提交用户信息
  const submitUserInfo = () => {
    let wxInfo = uni.getStorageSync("wxInfo")
    let wxConfig = JSON.parse(wxInfo)
    //构建参数
    let param = {
      wxOpenId: wxConfig.wxOpenId,
      sessionKey: wxConfig.sessionKey,
      unionId: wxConfig.unionId,
      nickname: nickname.value,
      avatar: avatar.value
    }
    initProfile(param).then(response => {
      if(response.code == 0) {
        //初始化成功，获取用户登录信息
        //根据type跳转
        userStore.setUserInfo(response.data)
        let type = uni.getStorageSync("EXCHANGE_TYPE") || null
        if(response.data.wxOpenId) {
          uni.setStorageSync("wxId", response.data.wxOpenId)
        }
        if(type == 1) {
          //跳转资源页面
          uni.removeStorageSync("EXCHANGE_TYPE")
          uni.reLaunch({
            url: '/pages/exchange/exchange'
          });
        } else if(type == 2) {
          // 跳转资源页面
          uni.removeStorageSync("EXCHANGE_TYPE")
          uni.reLaunch({
            url: '/pages/book/resource'
          });
        } else if(type ==3) {
          var forumId = uni.getStorageSync("forumId") || null
          //跳转论坛页面
          uni.reLaunch({
            url: '/pages/forum/forum?id=' + forumId
          });
          uni.removeStorageSync("EXCHANGE_TYPE")
        } else if(type ==4) {
          var id = uni.getStorageSync("tempForumId")
          uni.reLaunch({
            url: '/pages/forum/forum?id=' + id
          });
        } else {
          uni.reLaunch({
              url: '/pages/index/index'
          });
        }
      }
    })
  }
</script>

<style lang="scss" scoped>

  .title {
    font-weight: bold;
    font-size: 36rpx;
    text-align: center;
    margin: 100rpx 0rpx 80rpx 0;
  }

  .avatar-wrapper {
    height: 160rpx;
    width: 160rpx;
    border-radius: 50%;
    padding: 0;
    align-items: center;
    justify-content: center;
    display: flex;

    .avatar {
      width: 100%;
      height: 100%;
    }
  }

  .row {
    align-items: center;
    justify-content: center;
    display: flex;
    width: 100%;
    padding: 10rpx 10rpx;
    margin-bottom: 20rpx;

    .label {
      text-align: right;
      font-size: 32rpx;
      width: 35%;
    }

    .nickname {
      height: 90rpx;
      padding-left: 20rpx;
      text-align: left;
    }
  }

  .submit-btn {
    margin-top: 40rpx;
    border-radius: 10rpx;
    width: 90%;
    margin-left: 5%;
    color: white;
    text-align: center;
    line-height: 90rpx;
     background: linear-gradient(to right, #18e965, #23f58c);
    height: 90rpx;
  }
</style>
