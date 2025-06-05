import { defineStore } from 'pinia'
import { ref } from 'vue'

// 定义 Store
export const useUserStore = defineStore(
  'user',
  () => {
    // 登录用户信息
    const userInfo = ref(null)

    // 保存登录用户信息
    const setUserInfo = (val) => {
      userInfo.value = val
      uni.setStorageSync('token', val.accessToken)
    }

    // 清除登录用户信息
    const clearUserInfo = () => {
      userInfo.value = undefined
      uni.clearStorageSync()
    }

    // 记得 return
    return {
      userInfo,
      setUserInfo,
      clearUserInfo
    }
  },
  {
    unistorage: true, // 开启后对 state 数据读写都将持久化
  },
)
