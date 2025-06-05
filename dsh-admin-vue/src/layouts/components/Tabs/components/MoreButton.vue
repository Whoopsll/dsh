<template>
  <el-dropdown trigger="click" :teleported="false">
    <el-button size="small" type="primary">
      <span>{{ $t('tabs.more') }}</span>
      <el-icon class="el-icon--right"><arrow-down /></el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="refresh">
          <el-icon><Refresh /></el-icon>{{ $t('tabs.refresh') }}
        </el-dropdown-item>
        <el-dropdown-item @click="maximize">
          <el-icon><FullScreen /></el-icon>{{ $t('tabs.maximize') }}
        </el-dropdown-item>
        <el-dropdown-item divided @click="closeCurrentTab">
          <el-icon><Remove /></el-icon>{{ $t('tabs.closeCurrent') }}
        </el-dropdown-item>
        <el-dropdown-item @click="closeOtherTab">
          <el-icon><CircleClose /></el-icon>{{ $t('tabs.closeOther') }}
        </el-dropdown-item>
        <el-dropdown-item @click="closeAllTab">
          <el-icon><FolderDelete /></el-icon>{{ $t('tabs.closeAll') }}
        </el-dropdown-item>
        <el-dropdown-item @click="logout">
          <el-icon><SwitchButton /></el-icon>{{ $t('header.logout') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import { inject, nextTick } from 'vue'
import { useTabsStore } from '@/store/modules/tabs'
import { useAppStoreWithOut } from '@/store/modules/app'
import { logoutApi } from '@/api/modules/login'
import { LOGIN_URL } from '@/configs/config'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useKeepAliveStore } from '@/store/modules/keepAlive'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const tabStore = useTabsStore()
const appStore = useAppStoreWithOut()
const keepAliveStore = useKeepAliveStore()

// refresh current page
const refreshCurrentPage: Function = inject('refresh') as Function
const refresh = () => {
  setTimeout(() => {
    keepAliveStore.removeKeepAliveName(route.name as string)
    refreshCurrentPage(false)
    nextTick(() => {
      keepAliveStore.addKeepAliveName(route.name as string)
      refreshCurrentPage(true)
    })
  }, 0)
}

// maximize current page
const maximize = () => {
  appStore.setTheme('maximize', true)
}

// Close Current
const closeCurrentTab = () => {
  if (route.meta.isAffix) return
  tabStore.removeTabs(route.fullPath)
  keepAliveStore.removeKeepAliveName(route.name as string)
}

// Close Other
const closeOtherTab = () => {
  tabStore.closeMultipleTab(route.fullPath)
  keepAliveStore.setKeepAliveName([route.name] as string[])
}

// Close All
const closeAllTab = () => {
  tabStore.closeMultipleTab()
  keepAliveStore.setKeepAliveName()
  router.push(tabStore.tabsMenuList[0].path)
}

//logout
const logout = () => {
  tabStore.closeMultipleTab()
  keepAliveStore.setKeepAliveName()
  if (tabStore.tabsMenuList.length > 0) {
    router.push(tabStore.tabsMenuList[0].path)
  }
  //退出登录
  ElMessageBox.confirm('您是否确认退出登录?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 1.执行退出登录接口
    await logoutApi()

    // 2.清除 Token
    appStore.setToken('')

    // 3.重定向到登陆页
    router.replace(LOGIN_URL)
    ElMessage.success('退出登录成功！')
  })
}
</script>

<style scoped lang="less">
@import '../index.less';
</style>
