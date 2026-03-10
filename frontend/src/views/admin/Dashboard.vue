<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">数据分析</h2>

    <div v-if="authStore.isAdmin" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">用户总数</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.userCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">资源总数</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.resourceCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">帖子总数</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.forumCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">待审核资源</p>
        <p class="text-3xl font-bold text-primary-500 mt-2">{{ stats.pendingResourceCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">学生人数</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.studentCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">教师人数</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.teacherCount || 0 }}</p>
      </div>
      <div class="bg-white rounded-xl border border-gray-100 shadow-sm p-5">
        <p class="text-gray-400 text-sm">公告数量</p>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.noticeCount || 0 }}</p>
      </div>
    </div>

    <div v-else class="bg-white rounded-xl border border-gray-100 shadow-sm p-8 text-center">
      <h3 class="text-xl font-semibold text-gray-800 mb-2">欢迎使用管理后台</h3>
      <p class="text-gray-400">请使用左侧菜单导航到各管理功能页面</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminStats } from '@/api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const stats = ref({})

onMounted(async () => {
  if (authStore.isAdmin) {
    try {
      const res = await getAdminStats()
      if (res.code === 200) stats.value = res.data
    } catch (error) {
      console.error('获取统计数据失败:', error)
    }
  }
})
</script>
