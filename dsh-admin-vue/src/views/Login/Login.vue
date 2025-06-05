<template>
  <div class="login-container flx-center">
    <div class="system-title">出版社二维码全生命周期管控与内容审核系统</div>
    <div class="login-box">
      <SwitchDark class="dark" />
      <div class="login-left">
        <img class="login-left-img" src="@/assets/images/login_left.png" alt="login" />
      </div>
      <div class="login-form">
        <div class="login-logo">
          <h2 class="logo-text"> 系统登录 </h2>
        </div>
        <LoginForm />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="login">
import LoginForm from './components/LoginForm.vue'
import SwitchDark from '@/components/SwitchDark/index.vue'
</script>

<style scoped lang="less">
@import './index.less';
</style>
