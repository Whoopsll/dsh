import { defineStore } from 'pinia'
import { ref } from 'vue'

// 定义 Store
export const screenUseStore = defineStore(
  'screen',
  () => {
    // 顶部状态栏高度
    const statusBarHeight = ref(null)
    // 安全区底部高度
    const safeAreBottom = ref(null)

    //初始化
    const initSystemInfo = () => {
      const systemInfo = uni.getSystemInfoSync()
        // #ifdef H5
          statusBarHeight.value = 0
          safeAreBottom.value = 0
        // #endif
        // #ifdef MP-WEIXIN
        statusBarHeight.value = systemInfo.statusBarHeight || 20
        safeAreBottom.value = systemInfo.safeAreaInsets?.bottom || 0
        // #endif
    }

    // 记得 return
    return {
      statusBarHeight, safeAreBottom, initSystemInfo
    }
  }
)
