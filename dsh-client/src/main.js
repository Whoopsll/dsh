import { createSSRApp } from 'vue'
import * as Pinia from 'pinia'
import { createUnistorage } from 'pinia-plugin-unistorage'
import App from "./App.vue";

export function createApp() {
  const app = createSSRApp(App)
  const store = Pinia.createPinia()
  app.use(store)
  store.use(createUnistorage())
  return {
    app,
    Pinia, // 此处必须将 Pinia 返回
  }
}

// 只在 H5 平台处理 URL 重定向
// if (typeof window !== 'undefined' && window.location) {
//   const currentUrl = window.location.href;

//   if (!currentUrl.includes('/#/')) {
//     // 如果是 History 模式，重定向到 Hash 模式
//     uni.showToast({
//       title: window.location.pathname,
//       icon: 'none',
//     });
//     const newUrl = `${currentUrl.split('?')[0]}#${window.location.search}`;
//     window.location.replace(newUrl);
//   }
// }
