<template>
  <div class="min-h-[80vh] flex items-center justify-center">
    <div class="w-full max-w-md">
      <div class="text-center mb-8">
        <span class="text-primary-500 text-5xl font-black">K</span>
        <h2 class="text-2xl font-bold text-gray-800 mt-2">注册账号</h2>
        <p class="text-gray-400 mt-1">加入考研交流平台</p>
      </div>
      <div class="card">
        <form @submit.prevent="handleRegister">
          <div class="mb-4">
            <label class="block text-gray-600 mb-2 text-sm">用户名</label>
            <input v-model="form.username" type="text" class="input" placeholder="请输入用户名" required />
          </div>
          <div class="mb-4">
            <label class="block text-gray-600 mb-2 text-sm">邮箱</label>
            <input v-model="form.email" type="email" class="input" placeholder="请输入邮箱" required />
          </div>
          <div class="mb-4">
            <label class="block text-gray-600 mb-2 text-sm">密码</label>
            <input v-model="form.password" type="password" class="input" placeholder="请输入密码" required />
          </div>
          <div class="mb-6">
            <label class="block text-gray-600 mb-2 text-sm">确认密码</label>
            <input v-model="form.confirmPassword" type="password" class="input" placeholder="请再次输入密码" required />
          </div>
          <button type="submit" class="btn btn-primary w-full text-base py-3">
            注册
          </button>
        </form>
        <p class="text-center mt-5 text-gray-400 text-sm">
          已有账号？<router-link to="/login" class="text-primary-500 hover:text-primary-600 transition-colors">立即登录</router-link>
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
  email: '',
  password: '',
  confirmPassword: ''
})

const handleRegister = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    alert('两次密码输入不一致')
    return
  }
  const success = await authStore.register(form.value)
  if (success) {
    alert('注册成功，请登录')
    router.push('/login')
  } else {
    alert('注册失败')
  }
}
</script>
