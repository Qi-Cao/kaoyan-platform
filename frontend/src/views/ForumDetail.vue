<template>
  <div>
    <div class="mb-6">
      <router-link to="/forums" class="text-primary-500 hover:text-primary-600 transition-colors">&larr; 返回论坛</router-link>
    </div>

    <div v-if="forum" class="card mb-8">
      <h1 class="text-2xl font-bold text-gray-800 mb-2">{{ forum.title }}</h1>
      <div class="flex items-center space-x-4 text-sm text-gray-400 mb-4">
        <span>用户 #{{ forum.userId }}</span>
        <span>{{ formatDate(forum.createdAt) }}</span>
        <span>浏览 {{ forum.viewCount }}</span>
        <span>回复 {{ forum.replyCount }}</span>
      </div>
      <div class="text-gray-600 whitespace-pre-wrap leading-relaxed">{{ forum.content }}</div>
    </div>

    <div>
      <h2 class="text-xl font-bold text-gray-800 mb-4">回复 ({{ replies.length }})</h2>

      <div v-if="authStore.isLoggedIn" class="card mb-6">
        <textarea v-model="replyContent" class="input w-full" rows="3" placeholder="写下你的回复..."></textarea>
        <div class="flex justify-end mt-3">
          <button @click="submitReply" class="btn btn-primary" :disabled="!replyContent.trim()">发表回复</button>
        </div>
      </div>
      <p v-else class="text-gray-400 mb-4">
        <router-link to="/login" class="text-primary-500 hover:text-primary-600 transition-colors">登录</router-link> 后可以发表回复
      </p>

      <div class="space-y-4">
        <div v-for="reply in replies" :key="reply.id" :class="['card', reply.parentId ? 'ml-8' : '']">
          <div class="flex justify-between items-center mb-2">
            <span class="text-sm font-medium text-gray-700">用户 #{{ reply.userId }}</span>
            <span class="text-sm text-gray-400">{{ formatDate(reply.createdAt) }}</span>
          </div>
          <p class="text-gray-600">{{ reply.content }}</p>
        </div>
      </div>

      <div v-if="hasMoreReplies" class="flex justify-center mt-4">
        <button @click="loadMoreReplies" class="btn btn-secondary" :disabled="loadingReplies">
          {{ loadingReplies ? '加载中...' : '加载更多' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getForumDetail, getForumReplies, addForumReply } from '@/api'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const authStore = useAuthStore()
const forum = ref(null)
const replies = ref([])
const replyContent = ref('')
const replyPage = ref(1)
const hasMoreReplies = ref(true)
const loadingReplies = ref(false)

const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadForum = async () => {
  try {
    const res = await getForumDetail(route.params.id)
    forum.value = res.data
  } catch (error) {
    console.error('获取帖子详情失败:', error)
  }
}

const loadReplies = async () => {
  loadingReplies.value = true
  try {
    const res = await getForumReplies({ forumId: route.params.id, pageNum: 1, pageSize: 20 })
    replies.value = res.data.records || []
    hasMoreReplies.value = replies.value.length < (res.data.total || 0)
  } catch (error) {
    console.error('获取回复失败:', error)
  }
  loadingReplies.value = false
}

const loadMoreReplies = async () => {
  replyPage.value++
  loadingReplies.value = true
  try {
    const res = await getForumReplies({ forumId: route.params.id, pageNum: replyPage.value, pageSize: 20 })
    const newRecords = res.data.records || []
    replies.value = [...replies.value, ...newRecords]
    hasMoreReplies.value = replies.value.length < (res.data.total || 0)
  } catch (error) {
    console.error('获取回复失败:', error)
  }
  loadingReplies.value = false
}

const submitReply = async () => {
  if (!replyContent.value.trim()) return
  try {
    await addForumReply({ forumId: Number(route.params.id), content: replyContent.value, parentId: 0 })
    replyContent.value = ''
    replyPage.value = 1
    loadReplies()
  } catch (error) {
    alert('回复失败: ' + (error.message || '未知错误'))
  }
}

onMounted(() => { loadForum(); loadReplies() })
</script>
