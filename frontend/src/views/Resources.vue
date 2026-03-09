<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">考研资源</h2>
      <button v-if="authStore.isLoggedIn" @click="showAddModal = true" class="btn btn-primary">
        上传资源
      </button>
    </div>

    <!-- 分类筛选 -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button v-for="cat in categories" :key="cat" 
              @click="currentCategory = cat; searchResources()"
              :class="['px-4 py-2 rounded-full text-sm', 
                       currentCategory === cat ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200']">
        {{ cat }}
      </button>
    </div>

    <!-- 资源列表 -->
    <div class="space-y-4">
      <div v-for="resource in resources" :key="resource.id" class="card hover:shadow-md transition-shadow">
        <div class="flex justify-between items-start">
          <div>
            <h3 class="font-semibold text-lg text-gray-800">{{ resource.title }}</h3>
            <p class="text-gray-500 text-sm mt-1">{{ resource.category }} · {{ formatDate(resource.createdAt) }}</p>
          </div>
          <div class="flex items-center space-x-4 text-sm text-gray-500">
            <span>👁 {{ resource.viewCount }}</span>
            <span>⬇ {{ resource.downloadCount }}</span>
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
import { getResources, addResource } from '@/api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const resources = ref([])
const categories = ['全部', '真题', '笔记', '经验', '分数线']
const currentCategory = ref('全部')
const pageNum = ref(1)
const loading = ref(false)
const showAddModal = ref(false)

const searchResources = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getResources({ 
      pageNum: 1, 
      pageSize: 10, 
      category: currentCategory.value === '全部' ? '' : currentCategory.value 
    })
    resources.value = res.data.records || []
  } catch (error) {
    console.error('获取资源列表失败:', error)
  }
  loading.value = false
}

const loadMore = async () => {
  pageNum.value++
  loading.value = true
  try {
    const res = await getResources({ 
      pageNum: pageNum.value, 
      pageSize: 10, 
      category: currentCategory.value === '全部' ? '' : currentCategory.value 
    })
    resources.value = [...resources.value, ...(res.data.records || [])]
  } catch (error) {
    console.error('获取资源列表失败:', error)
  }
  loading.value = false
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  searchResources()
})
</script>
