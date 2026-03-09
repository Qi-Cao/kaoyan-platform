<template>
  <div class="max-w-md mx-auto">
    <div class="card">
      <h2 class="text-2xl font-bold text-center mb-6">登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">用户名</label>
          <input v-model="form.username" type="text" class="input" placeholder="请输入用户名" required />
        </div>
        <div class="mb-6">
          <label class="block text-gray-700 mb-2">密码</label>
          <input v-model="form.password" type="password" class="input" placeholder="请输入密码" required />
        </div>
        <button type="submit" class="btn btn-primary w-full">
          登录
        </button>
      </form>
      <p class="text-center mt-4 text-gray-600">
        还没有账号？<router-link to="/register" class="text-primary-600 hover:underline">立即注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  const success = await authStore.login(form.value.username, form.value.password)
  if (success) {
    router.push('/')
  } else {
    alert('登录失败，请检查用户名和密码')
  }
}
</script>
