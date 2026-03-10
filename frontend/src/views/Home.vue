<template>
  <div>
    <!-- 公告区 -->
    <div class="mb-8">
      <h2 class="text-2xl font-bold text-gray-800 mb-4">最新公告</h2>
      <div class="space-y-3">
        <div v-for="notice in notices" :key="notice.id" 
             class="card card-hover cursor-pointer">
          <div class="flex items-center justify-between">
            <h3 class="font-semibold text-lg text-gray-800">{{ notice.title }}</h3>
            <span v-if="notice.isTop" class="text-xs bg-primary-500 text-white px-2 py-0.5 rounded-full">置顶</span>
          </div>
          <p class="text-gray-500 mt-2 line-clamp-2">{{ notice.content }}</p>
          <span class="text-sm text-gray-400 mt-2 block">
            {{ formatDate(notice.createdAt) }}
          </span>
        </div>
        <div v-if="notices.length === 0" class="text-center text-gray-400 py-8">暂无公告</div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
      <router-link to="/schools" class="card card-hover text-center group">
        <div class="text-4xl mb-3">🏫</div>
        <h3 class="font-semibold text-gray-800 group-hover:text-primary-500 transition-colors">院校库</h3>
        <p class="text-sm text-gray-400 mt-1">查看院校信息</p>
      </router-link>
      <router-link to="/resources" class="card card-hover text-center group">
        <div class="text-4xl mb-3">📚</div>
        <h3 class="font-semibold text-gray-800 group-hover:text-primary-500 transition-colors">考研资源</h3>
        <p class="text-sm text-gray-400 mt-1">真题笔记经验</p>
      </router-link>
      <router-link to="/forums" class="card card-hover text-center group">
        <div class="text-4xl mb-3">💬</div>
        <h3 class="font-semibold text-gray-800 group-hover:text-primary-500 transition-colors">交流论坛</h3>
        <p class="text-sm text-gray-400 mt-1">考研经验分享</p>
      </router-link>
      <router-link to="/register" class="card card-hover text-center group">
        <div class="text-4xl mb-3">👥</div>
        <h3 class="font-semibold text-gray-800 group-hover:text-primary-500 transition-colors">加入我们</h3>
        <p class="text-sm text-gray-400 mt-1">注册成为会员</p>
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
