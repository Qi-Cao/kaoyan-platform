<template>
  <!-- 管理后台路由 -->
  <router-view v-if="isAdminRoute" />

  <!-- 学生端路由 -->
  <div v-else class="min-h-screen bg-gray-50 flex flex-col">
    <nav class="bg-white border-b border-gray-100 sticky top-0 z-50 shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-14">
          <div class="flex items-center">
            <router-link to="/" class="flex items-center space-x-2">
              <span class="text-primary-500 text-2xl font-black">K</span>
              <span class="text-gray-800 font-bold text-lg">考研交流</span>
            </router-link>
            <div class="hidden md:flex ml-10 space-x-1">
              <router-link to="/" class="nav-link" active-class="nav-link-active">
                首页
              </router-link>
              <router-link to="/schools" class="nav-link" active-class="nav-link-active">
                院校库
              </router-link>
              <router-link to="/resources" class="nav-link" active-class="nav-link-active">
                资源
              </router-link>
              <router-link to="/forums" class="nav-link" active-class="nav-link-active">
                论坛
              </router-link>
            </div>
          </div>
          <div class="flex items-center space-x-3">
            <template v-if="!authStore.isLoggedIn">
              <router-link to="/login" class="btn btn-secondary text-sm">
                登录
              </router-link>
              <router-link to="/register" class="btn btn-primary text-sm">
                注册
              </router-link>
            </template>
            <template v-else>
              <router-link to="/profile" class="nav-link text-sm">
                个人中心
              </router-link>
              <router-link v-if="authStore.isAdmin || authStore.isTeacher" to="/admin/dashboard" class="btn btn-primary text-sm">
                管理后台
              </router-link>
              <span class="text-gray-500 text-sm">{{ authStore.user?.username }}</span>
              <button @click="handleLogout" class="btn btn-secondary text-sm">
                退出
              </button>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 flex-1 w-full">
      <router-view />
    </main>

    <footer class="bg-white border-t border-gray-100 py-6">
      <div class="max-w-7xl mx-auto px-4 text-center text-gray-400 text-sm">
        &copy; 2026 考研信息交流平台
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from './stores/auth'
import { useRouter, useRoute } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const isAdminRoute = computed(() => route.path.startsWith('/admin'))

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.nav-link {
  @apply px-3 py-1.5 rounded-lg text-sm text-gray-500 hover:text-primary-500 hover:bg-gray-50 transition-all;
}
.nav-link-active {
  @apply text-primary-500 bg-primary-50 font-medium;
}
</style>
