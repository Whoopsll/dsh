import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
// https://vitejs.dev/config/
export default defineConfig({
  // base: '/dsh-h5/', //南师大
  base: '/dsh-client-h5/',
  plugins: [
    uni(),
  ]
})
