<template>
  <div>
    <div class="mb-6">
      <router-link to="/resources" class="text-primary-500 hover:text-primary-600 transition-colors">&larr; 返回资源列表</router-link>
    </div>

    <div v-if="resource" class="card">
      <div class="flex justify-between items-start mb-4">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">{{ resource.title }}</h1>
          <p class="text-gray-400 mt-1">{{ resource.category }} &middot; {{ formatDate(resource.createdAt) }}</p>
        </div>
        <div class="flex items-center space-x-4 text-sm text-gray-400">
          <span>浏览 {{ resource.viewCount }}</span>
          <span>下载 {{ resource.downloadCount }}</span>
        </div>
      </div>
      <div class="text-gray-600 mb-6 whitespace-pre-wrap leading-relaxed">{{ resource.content }}</div>
      <div v-if="resource.fileUrl" class="mb-6">
        <a :href="resource.fileUrl" target="_blank" class="btn btn-primary">下载资源文件</a>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="mt-8">
      <h2 class="text-xl font-bold text-gray-800 mb-4">评论 ({{ comments.length }})</h2>

      <div v-if="authStore.isLoggedIn" class="card mb-6">
        <textarea v-model="commentContent" class="input w-full" rows="3" placeholder="写下你的评论..."></textarea>
        <div class="flex justify-end mt-3">
          <button @click="submitComment" class="btn btn-primary" :disabled="!commentContent.trim()">发表评论</button>
        </div>
      </div>
      <p v-else class="text-gray-400 mb-4">
        <router-link to="/login" class="text-primary-500 hover:text-primary-600 transition-colors">登录</router-link> 后可以发表评论
      </p>

      <div class="space-y-4">
        <div v-for="comment in comments" :key="comment.id" class="card">
          <div class="flex justify-between items-center mb-2">
            <span class="text-sm font-medium text-gray-700">用户 #{{ comment.userId }}</span>
            <span class="text-sm text-gray-400">{{ formatDate(comment.createdAt) }}</span>
          </div>
          <p class="text-gray-600">{{ comment.content }}</p>
        </div>
      </div>

      <div v-if="hasMoreComments" class="flex justify-center mt-4">
        <button @click="loadMoreComments" class="btn btn-secondary" :disabled="loadingComments">
          {{ loadingComments ? '加载中...' : '加载更多' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getResourceDetail, getResourceComments, addResourceComment } from '@/api'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const authStore = useAuthStore()
const resource = ref(null)
const comments = ref([])
const commentContent = ref('')
const commentPage = ref(1)
const hasMoreComments = ref(true)
const loadingComments = ref(false)

const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadResource = async () => {
  try {
    const res = await getResourceDetail(route.params.id)
    resource.value = res.data
  } catch (error) {
    console.error('获取资源详情失败:', error)
  }
}

const loadComments = async () => {
  loadingComments.value = true
  try {
    const res = await getResourceComments({ resourceId: route.params.id, pageNum: 1, pageSize: 10 })
    comments.value = res.data.records || []
    hasMoreComments.value = comments.value.length < (res.data.total || 0)
  } catch (error) {
    console.error('获取评论失败:', error)
  }
  loadingComments.value = false
}

const loadMoreComments = async () => {
  commentPage.value++
  loadingComments.value = true
  try {
    const res = await getResourceComments({ resourceId: route.params.id, pageNum: commentPage.value, pageSize: 10 })
    const newRecords = res.data.records || []
    comments.value = [...comments.value, ...newRecords]
    hasMoreComments.value = comments.value.length < (res.data.total || 0)
  } catch (error) {
    console.error('获取评论失败:', error)
  }
  loadingComments.value = false
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return
  try {
    await addResourceComment({ resourceId: Number(route.params.id), content: commentContent.value })
    commentContent.value = ''
    commentPage.value = 1
    loadComments()
  } catch (error) {
    alert('评论失败: ' + (error.message || '未知错误'))
  }
}

onMounted(() => { loadResource(); loadComments() })
</script>
