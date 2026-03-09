<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">交流论坛</h2>
      <button v-if="authStore.isLoggedIn" @click="showPostModal = true" class="btn btn-primary">
        发布帖子
      </button>
    </div>

    <!-- 分类筛选 -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button v-for="cat in forumCategories" :key="cat.id" 
              @click="currentCategory = cat.id; searchForums()"
              :class="['px-4 py-2 rounded-full text-sm', 
                       currentCategory === cat.id ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
        {{ cat.name }}
      </button>
    </div>

    <!-- 帖子列表 -->
    <div class="space-y-4">
      <div v-for="forum in forums" :key="forum.id" class="card hover:shadow-md transition-shadow cursor-pointer">
        <h3 class="font-semibold text-lg text-gray-800">{{ forum.title }}</h3>
        <p class="text-gray-500 text-sm mt-2 line-clamp-2">{{ forum.content }}</p>
        <div class="flex items-center justify-between mt-4 text-sm text-gray-500">
          <span>{{ formatDate(forum.createdAt) }}</span>
          <div class="flex items-center space-x-4">
            <span>👁 {{ forum.viewCount }}</span>
            <span>💬 {{ forum.replyCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载更多 -->
    <div class="flex justify-center mt-8">
      <button @click="loadMore" class="btn btn-secondary" :disabled="loading">
        {{ loading ? '加载中...' : '加载更多' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getForums, getForumCategories } from '@/api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const forums = ref([])
const forumCategories = ref([])
const currentCategory = ref(null)
const pageNum = ref(1)
const loading = ref(false)
const showPostModal = ref(false)

const searchForums = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getForums({ 
      pageNum: 1, 
      pageSize: 10, 
      categoryId: currentCategory.value 
    })
    forums.value = res.data.records || []
  } catch (error) {
    console.error('获取帖子列表失败:', error)
  }
  loading.value = false
}

const loadMore = async () => {
  pageNum.value++
  loading.value = true
  try {
    const res = await getForums({ 
      pageNum: pageNum.value, 
      pageSize: 10, 
      categoryId: currentCategory.value 
    })
    forums.value = [...forums.value, ...(res.data.records || [])]
  } catch (error) {
    console.error('获取帖子列表失败:', error)
  }
  loading.value = false
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(async () => {
  searchForums()
  try {
    const res = await getForumCategories()
    forumCategories.value = res.data || []
  } catch (error) {
    console.error('获取分类失败:', error)
  }
})
</script>
