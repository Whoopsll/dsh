<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { resource } from '@/service/book'
import { baseWxRedirectUrl } from '@/constant/baseInfo'

const flag = ref(false)
const wxOpenId = ref()
const resourceId = ref()
const codeSecret = ref()

onLoad((e) => {
  resourceId.value = uni.getStorageSync('resourceId') || null
  codeSecret.value = uni.getStorageSync('codeSecret') || null
  if (e.resourceId) {
    uni.setStorageSync('resourceId', e.resourceId)
    resourceId.value = e.resourceId
  }
  if (e.secret) {
    uni.setStorageSync('codeSecret', e.secret)
    codeSecret.value = e.secret
  }
  wxOpenId.value = uni.getStorageSync('wxId') || null
  // wxOpenId.value = 'oXh4A69AMAxh85jDRVI6KA17cOvM'
  // uni.showToast({
  //   icon: 'error',
  //   title: wxOpenId.value,
  //   duration: 300,
  // })
  if (wxOpenId.value != null) {
    checkVerifyAvailable(resourceId.value, codeSecret.value)
  } else if (e.wxId) {
    wxOpenId.value = e.wxId
    checkVerifyAvailable(resourceId.value, codeSecret.value)
  } else {
    wxCheck()
  }
})

const wxCheck = () => {
  // const appid = 'wx731630b450248376'
  const appid = 'wx0055deef26d951ff'
  const redirect_uri = encodeURIComponent(
    baseWxRedirectUrl + '/pages/exchange/exchange-temp?type=2',
  )
  const scope = 'snsapi_userinfo'
  const url = ref(
    `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${redirect_uri}&response_type=code&scope=${scope}&state=STATE#wechat_redirect`,
  )
  window.location.href = url.value
}
const checkVerifyAvailable = (resourceId, codeSecret) => {
  const dto = {
    resourceId: resourceId,
    codeSecret: codeSecret,
    openId: wxOpenId.value,
  }
  resource(dto).then((res) => {
    if (res.code === 0) {
      window.location.href = res.data
    } else {
      flag.value = true
    }
  })
}
</script>

<template>
  <view v-if="flag"> 资源验证失败！ </view>
  <view v-else> 跳转中... </view>
</template>
