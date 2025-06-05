<template>
  <view class="loginPage"> </view>
</template>

<script lang="js" setup>
import { wxLogin } from '@/service/auth'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()

let currentUri = window.location.href
let params = currentUri.split('?')[1]
let code = params.split('&')[0].split('=')[1]

wxLogin(code).then(res => {
  userStore.setUserInfo(res.data)
  let type = uni.getStorageSync("EXCHANGE_TYPE") || null
  if(res.data.wxOpenId) {
    uni.setStorageSync("wxId", res.data.wxOpenId)
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
})
</script>

<style lang="scss" scoped>
page {
  height: 100%;
}
</style>
