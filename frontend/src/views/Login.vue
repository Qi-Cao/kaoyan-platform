<template>
  <div class="min-h-[80vh] flex items-center justify-center">
    <div class="w-full max-w-md">
      <div class="text-center mb-8">
        <span class="text-primary-500 text-5xl font-black">K</span>
        <h2 class="text-2xl font-bold text-gray-800 mt-2">登录考研交流平台</h2>
        <p class="text-gray-400 mt-1">欢迎回来</p>
      </div>
      <div class="card">
        <form @submit.prevent="handleLogin">
          <div class="mb-4">
            <label class="block text-gray-600 mb-2 text-sm">用户名</label>
            <input v-model="form.username" type="text" class="input" placeholder="请输入用户名" required />
          </div>
          <div class="mb-6">
            <label class="block text-gray-600 mb-2 text-sm">密码</label>
            <input v-model="form.password" type="password" class="input" placeholder="请输入密码" required />
          </div>
          <button type="submit" class="btn btn-primary w-full text-base py-3">
            登录
          </button>
        </form>
        <p class="text-center mt-5 text-gray-400 text-sm">
          还没有账号？<router-link to="/register" class="text-primary-500 hover:text-primary-600 transition-colors">立即注册</router-link>
        </p>
      </div>
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
  try {
    const success = await authStore.login(form.value.username, form.value.password)
    if (success) {
      const role = authStore.userRole
      if (role === 'ADMIN' || role === 'TEACHER') {
        router.push('/admin/dashboard')
      } else {
        router.push('/')
      }
    } else {
      alert('登录失败，请检查用户名和密码')
    }
  } catch (error) {
    alert('登录出错: ' + (error.message || '未知错误'))
  }
}
</script>
