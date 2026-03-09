<template>
  <div class="min-h-screen bg-gray-50">
    <nav class="bg-white shadow-sm border-b border-gray-100">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <router-link to="/" class="text-xl font-bold text-primary-600">
              考研信息交流平台
            </router-link>
            <div class="hidden md:flex ml-10 space-x-8">
              <router-link to="/" class="text-gray-700 hover:text-primary-600 px-3 py-2">
                首页
              </router-link>
              <router-link to="/schools" class="text-gray-700 hover:text-primary-600 px-3 py-2">
                院校库
              </router-link>
              <router-link to="/resources" class="text-gray-700 hover:text-primary-600 px-3 py-2">
                资源
              </router-link>
              <router-link to="/forums" class="text-gray-700 hover:text-primary-600 px-3 py-2">
                论坛
              </router-link>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <template v-if="!authStore.isLoggedIn">
              <router-link to="/login" class="btn btn-secondary">
                登录
              </router-link>
              <router-link to="/register" class="btn btn-primary">
                注册
              </router-link>
            </template>
            <template v-else>
              <span class="text-gray-700">{{ authStore.user?.username }}</span>
              <button @click="handleLogout" class="btn btn-secondary">
                退出
              </button>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <router-view />
    </main>

    <footer class="bg-white border-t border-gray-100 py-6 mt-auto">
      <div class="max-w-7xl mx-auto px-4 text-center text-gray-500">
        © 2026 考研信息交流平台. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>
