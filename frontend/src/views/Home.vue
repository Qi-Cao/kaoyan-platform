<template>
  <div>
    <!-- 公告区 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold text-gray-800 mb-4">最新公告</h2>
      <div class="space-y-3">
        <div v-for="notice in notices" :key="notice.id" 
             class="card hover:shadow-md transition-shadow cursor-pointer">
          <h3 class="font-semibold text-lg text-gray-800">{{ notice.title }}</h3>
          <p class="text-gray-600 mt-2 line-clamp-2">{{ notice.content }}</p>
          <span class="text-sm text-gray-400 mt-2 block">
            {{ formatDate(notice.createdAt) }}
          </span>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
      <router-link to="/schools" class="card hover:shadow-md transition-shadow text-center">
        <div class="text-4xl mb-2">🏫</div>
        <h3 class="font-semibold text-gray-800">院校库</h3>
        <p class="text-sm text-gray-500">查看院校信息</p>
      </router-link>
      <router-link to="/resources" class="card hover:shadow-md transition-shadow text-center">
        <div class="text-4xl mb-2">📚</div>
        <h3 class="font-semibold text-gray-800">考研资源</h3>
        <p class="text-sm text-gray-500">真题笔记经验</p>
      </router-link>
      <router-link to="/forums" class="card hover:shadow-md transition-shadow text-center">
        <div class="text-4xl mb-2">💬</div>
        <h3 class="font-semibold text-gray-800">交流论坛</h3>
        <p class="text-sm text-gray-500">考研经验分享</p>
      </router-link>
      <router-link to="/register" class="card hover:shadow-md transition-shadow text-center">
        <div class="text-4xl mb-2">👥</div>
        <h3 class="font-semibold text-gray-800">加入我们</h3>
        <p class="text-sm text-gray-500">注册成为会员</p>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNotices } from '@/api'

const notices = ref([])

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(async () => {
  try {
    const res = await getNotices()
    notices.value = res.data || []
  } catch (error) {
    console.error('获取公告失败:', error)
  }
})
</script>
