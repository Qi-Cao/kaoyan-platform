<template>
  <div class="flex h-screen bg-gray-50">
    <!-- 侧边栏 -->
    <div class="w-64 bg-white border-r border-gray-100 flex flex-col shadow-sm">
      <div class="p-5 border-b border-gray-100">
        <div class="flex items-center space-x-2">
          <span class="text-primary-500 text-2xl font-black">K</span>
          <span class="text-gray-800 font-bold">管理后台</span>
        </div>
        <p class="text-gray-400 text-sm mt-2">{{ authStore.user?.username }}</p>
      </div>
      <nav class="flex-1 py-4 overflow-y-auto">
        <router-link v-if="authStore.isAdmin" to="/admin/dashboard" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">📊</span> 数据分析
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/users" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">👥</span> 用户管理
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/teachers" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">🎓</span> 教师管理
        </router-link>
        <router-link to="/admin/resources" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">📚</span> 资源管理
        </router-link>
        <router-link to="/admin/forums" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">💬</span> 论坛管理
        </router-link>
        <router-link to="/admin/schools" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">🏫</span> 院校管理
        </router-link>
        <router-link to="/admin/notices" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">📢</span> 公告管理
        </router-link>
        <router-link to="/admin/comments" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">💭</span> 资源留言
        </router-link>
        <router-link to="/admin/profile" class="nav-item" active-class="nav-item-active">
          <span class="mr-2">⚙</span> 个人中心
        </router-link>
      </nav>
      <div class="p-4 border-t border-gray-100 space-y-2">
        <router-link to="/" class="block text-gray-400 hover:text-primary-500 text-sm transition-colors">
          &larr; 返回前台
        </router-link>
        <button @click="handleLogout" class="text-gray-400 hover:text-primary-500 text-sm transition-colors">
          退出登录
        </button>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.nav-item {
  display: flex;
  align-items: center;
  padding: 0.6rem 1.25rem;
  margin: 0.125rem 0.75rem;
  border-radius: 0.5rem;
  color: #6b7280;
  font-size: 0.875rem;
  transition: all 0.2s;
}
.nav-item:hover {
  background-color: #f9fafb;
  color: #374151;
}
.nav-item-active {
  background-color: #fff1f3;
  color: #FE2C55;
  font-weight: 500;
}
</style>
