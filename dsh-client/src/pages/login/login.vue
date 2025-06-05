<template>
  <view class="loginPage">
    <SafeAreaWrapper title="登录" :childPage="true">
      <view class="form">
        <button class="button" @click="submitWxLogin">
          <uni-icons type="weixin" color="#005538" size="20"></uni-icons>
          微信一键登录
        </button>
      </view>
    </SafeAreaWrapper>
  </view>
</template>

<script lang="js" setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { wxLogin,wxMiniLogin } from '@/service/auth'
import { baseWxRedirectUrl } from '@/constant/baseInfo'
import { useUserStore } from '@/stores/user'
import SafeAreaWrapper from '@/components/SafeAreaWrapper/SafeAreaWrapper.vue'
const userStore = useUserStore()
onLoad((e) => {
  if(e.type) {
    uni.setStorageSync('EXCHANGE_TYPE', e.type)
    if(e.type == 3) {
      //扫码进入书籍页面
      uni.setStorageSync('EXCHANGE_TYPE', e.type)
      uni.setStorageSync('forumId', e.forumId)
    }
    if(e.type == 4) {
      uni.setStorageSync('EXCHANGE_TYPE', e.type)
      uni.setStorageSync('tempForumId', e.forumId)
    }

  }
  if(e.cardNum) {
    uni.setStorageSync('cardNum', e.cardNum)
  }
  if(e.codeSecret) {
    uni.setStorageSync('codeSecret', e.codeSecret)
    uni.setStorageSync('resourceId', e.resourceId)
  }
})
// import wx from "weixin-js-sdk";
/**
 * 微信登录
 */

// wx.config({
//   debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
//   appId: 'wx731630b450248376', // 必填，公众号的唯一标识
//   timestamp: new Date().getTime(), // 必填，生成签名的时间戳
//   nonceStr: 'dsh', // 必填，生成签名的随机串
//   signature: 'jsapi_ticket',// 必填，签名
//   jsApiList: ['chooseImage'] // 必填，需要使用的JS接口列表
// });

const submitWxLogin = () => {
  // const appid = 'wx731630b450248376'

  const appid = 'wx0055deef26d951ff'
  const redirect_uri = encodeURIComponent( baseWxRedirectUrl +'/pages/login/login-temp')
  // const redirect_uri = encodeURIComponent('http://192.168.0.190:5173/#/pages/login/login-temp')
  const scope = 'snsapi_userinfo'
  const url = ref(`https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${redirect_uri}&response_type=code&scope=${scope}&state=STATE#wechat_redirect`)
  // #ifdef H5

    // 在 H5 上使用微信的 OAuth 授权
    window.location.href = url.value
    // #endif

    // #ifdef MP
    // 在微信小程序上使用 wx.login 获取 code
    wx.login({
      success: (res) => {
        if (res.code) {
          // 获取到登录凭证 code，发送给服务器换取 session_key 和 openid
          // 调用服务器接口，后台根据 code 获取用户的 openid 和 session_key
          //判断该用户是否为新用户，若为新用户则跳转initProfile页面初始化用户信息，否则直接根据type跳转登录
          wxMiniLogin(res.code).then(response => {
            if(response.code == 0) {
              //判断是否存在sessionKey，若存在，则表示该用户不存在
              if(response.data.sessionKey != null) {
                //跳转初始化用户信息页面
                uni.setStorageSync('wxInfo', JSON.stringify(response.data));
                uni.navigateTo({
                  url: '/pages/initProfile/initProfile'
                })
              } else {
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
            }
          })
        } else {
          console.error('微信小程序登录失败', res.errMsg)
        }
      },
      fail: (err) => {
        console.error('wx.login 调用失败', err)
      }
    })
    // #endif
}

const wxLoginMock = () => {
  uni.setStorageSync('token', "tokentokentokentoken")
  uni.reLaunch({
     url: '/pages/index/index'
  });
}
</script>

<style lang="scss" scoped>
.loginPage {
  display: flex;
  flex-direction: column;
  .top {
    .title {
      font-size: 48rpx;
      color: #000;
      margin-bottom: 30rpx;
    }
    .info {
      font-size: 24rpx;
      color: #999;
    }
  }

  .form {
    padding: 80rpx 0;
    margin-top: 100rpx;

    .input {
      height: 100rpx;
      font-size: 28rpx;
      border-bottom: 1px solid #ddd;
      margin-bottom: 20rpx;
    }

    .button {
      border-radius: 10rpx;
      display: flex;
      align-items: center;
      height: 100rpx;
      justify-content: center;
      margin: 0px 20rpx 0 20rpx;
      font-size: 32rpx;
      background: linear-gradient(to right, #18e965, #23f58c);
      color: #fff;
    }
  }
}
</style>
