<template>
  <view></view>
</template>

<script lang="js" setup>
import { wxVerify } from '@/service/auth'
import { onLoad } from '@dcloudio/uni-app'

onLoad((e) => {
  let currentUri = window.location.href
  // alert(currentUri)
  let params = currentUri.split('?')[1]
  let code = params.split('&')[0].split('=')[1]
  wxVerify(code).then(res => {
    uni.showToast({
      title: "验证成功" + res.data,
      icon: 'none'
    })
    uni.setStorageSync("wxId", res.data)
    if(e.type) {
      uni.reLaunch({
        url: `/pages/book/resource?wxId=${res.data}`
      });
    } else {
      uni.reLaunch({
      url: `/pages/exchange/exchange?wxId=${res.data}`
    });
    }
  })
})
</script>

<style lang="scss" scoped>
page {
  height: 100%;
}
</style>
