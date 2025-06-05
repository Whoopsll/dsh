<script>
  import { screenUseStore } from '@/stores/screen'
export default {
  onLaunch: function () {
    console.log('App Launch')
  },
  onShow: function () {
    const screenStore = screenUseStore();
    screenStore.initSystemInfo()
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
}
</script>

<style>
/*每个页面公共css */
scroll-view ::-webkit-scrollbar {
  width: 0;
  height: 0;
  background-color: transparent;
}

/* #ifdef H5 */
* {
  -webkit-tap-highlight-color: transparent; /* 去掉 iOS 点击高亮 */
  outline: none; /* 去掉聚焦轮廓 */
}
/* #endif */

/* #ifdef MP-WEIXIN */
page {
  -webkit-tap-highlight-color: transparent;
  outline: none;
}
/* #endif */

/* 如果需要去除所有元素的背景变化，可以添加以下规则 */

/* #ifdef H5*/
*:active {
  background-color: transparent; /* 点击时背景颜色 */
  color: inherit; /* 点击时字体颜色 */
}
/* #endif */

/* #ifdef MP-WEIXIN */
page:active {
  background-color: transparent; /* 点击时背景颜色 */
  color: inherit; /* 点击时字体颜色 */
}
/* #endif */
</style>
