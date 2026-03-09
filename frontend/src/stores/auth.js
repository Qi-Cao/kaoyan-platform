import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  
  const isLoggedIn = computed(() => !!token.value)
  
  const login = async (username, password) => {
    const res = await api.login(username, password)
    if (res.code === 200) {
      token.value = res.data.token
      user.value = res.data.user
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data.user))
      return true
    }
    return false
  }
  
  const register = async (data) => {
    const res = await api.register(data)
    return res.code === 200
  }
  
  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token.removeItem('user')
    localStorage')
  }
  
  return {
    token,
    user,
    isLoggedIn,
    login,
    register,
    logout
  }
})
