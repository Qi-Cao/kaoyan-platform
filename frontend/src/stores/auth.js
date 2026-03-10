import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(null)
  
  // 安全地解析用户信息
  try {
    const userData = localStorage.getItem('user')
    if (userData && userData !== 'null') {
      user.value = JSON.parse(userData)
    }
  } catch (e) {
    user.value = null
  }
  
  const isLoggedIn = computed(() => !!token.value)
  const userRole = computed(() => user.value?.roleName || 'STUDENT')
  const isAdmin = computed(() => userRole.value === 'ADMIN')
  const isTeacher = computed(() => userRole.value === 'TEACHER')
  const isStudent = computed(() => userRole.value === 'STUDENT')
  
  const login = async (username, password) => {
    try {
      const res = await loginApi(username, password)
      console.log('登录响应:', res)
      if (res.code === 200) {
        token.value = res.data.token
        user.value = res.data.user
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data.user))
        return true
      }
      return false
    } catch (error) {
      console.error('登录请求失败:', error)
      return false
    }
  }
  
  const register = async (data) => {
    try {
      const res = await registerApi(data)
      return res.code === 200
    } catch (error) {
      console.error('注册请求失败:', error)
      return false
    }
  }
  
  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
  
  return {
    token,
    user,
    isLoggedIn,
    userRole,
    isAdmin,
    isTeacher,
    isStudent,
    login,
    register,
    logout
  }
})
