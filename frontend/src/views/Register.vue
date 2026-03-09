<template>
  <div class="max-w-md mx-auto">
    <div class="card">
      <h2 class="text-2xl font-bold text-center mb-6">注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">用户名</label>
          <input v-model="form.username" type="text" class="input" placeholder="请输入用户名" required />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">邮箱</label>
          <input v-model="form.email" type="email" class="input" placeholder="请输入邮箱" required />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">密码</label>
          <input v-model="form.password" type="password" class="input" placeholder="请输入密码" required />
        </div>
        <div class="mb-6">
          <label class="block text-gray-700 mb-2">确认密码</label>
          <input v-model="form.confirmPassword" type="password" class="input" placeholder="请再次输入密码" required />
        </div>
        <button type="submit" class="btn btn-primary w-full">
          注册
        </button>
      </form>
      <p class="text-center mt-4 text-gray-600">
        已有账号？<router-link to="/login" class="text-primary-600 hover:underline">立即登录</router-link>
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
