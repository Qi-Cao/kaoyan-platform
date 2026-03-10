<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">交流论坛</h2>
      <button v-if="authStore.isLoggedIn" @click="showPostModal = true" class="btn btn-primary">发布帖子</button>
    </div>

    <!-- 分类筛选 -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button @click="currentCategory = null; searchForums()"
              :class="['px-4 py-2 rounded-lg text-sm transition-all', 
                       currentCategory === null ? 'bg-primary-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-primary-300 hover:text-primary-500']">
        全部
      </button>
      <button v-for="cat in forumCategories" :key="cat.id" 
              @click="currentCategory = cat.id; searchForums()"
              :class="['px-4 py-2 rounded-lg text-sm transition-all', 
                       currentCategory === cat.id ? 'bg-primary-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-primary-300 hover:text-primary-500']">
        {{ cat.name }}
      </button>
    </div>

    <!-- 帖子列表 -->
    <div class="space-y-4">
      <div v-for="forum in forums" :key="forum.id" 
           class="card card-hover cursor-pointer"
           @click="$router.push('/forums/' + forum.id)">
        <h3 class="font-semibold text-lg text-gray-800">{{ forum.title }}</h3>
        <p class="text-gray-400 text-sm mt-2 line-clamp-2">{{ forum.content }}</p>
        <div class="flex items-center justify-between mt-4 text-sm text-gray-400">
          <span>{{ formatDate(forum.createdAt) }}</span>
          <div class="flex items-center space-x-4">
            <span>浏览 {{ forum.viewCount }}</span>
            <span>回复 {{ forum.replyCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="forums.length === 0" class="text-center text-gray-400 py-16">暂无数据</div>

    <div class="flex justify-center mt-8">
      <button @click="loadMore" class="btn btn-secondary" :disabled="loading">
        {{ loading ? '加载中...' : '加载更多' }}
      </button>
    </div>

    <!-- 发布帖子模态框 -->
    <div v-if="showPostModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-lg mx-4">
        <h3 class="text-xl font-bold text-gray-800 mb-4">发布帖子</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-gray-600 mb-1 text-sm">标题</label>
            <input v-model="postForm.title" class="input" placeholder="请输入帖子标题" />
          </div>
          <div>
            <label class="block text-gray-600 mb-1 text-sm">分类</label>
            <select v-model="postForm.categoryId" class="input">
              <option v-for="cat in forumCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-gray-600 mb-1 text-sm">内容</label>
            <textarea v-model="postForm.content" class="input" rows="6" placeholder="请输入帖子内容"></textarea>
          </div>
        </div>
        <div class="flex justify-end space-x-4 mt-6">
          <button @click="showPostModal = false" class="btn btn-secondary">取消</button>
          <button @click="submitPost" class="btn btn-primary">发布</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getForums, getForumCategories, createForum } from '@/api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const forums = ref([])
const forumCategories = ref([])
const currentCategory = ref(null)
const pageNum = ref(1)
const loading = ref(false)
const showPostModal = ref(false)
const postForm = ref({ title: '', categoryId: null, content: '' })

const submitPost = async () => {
  if (!postForm.value.title.trim() || !postForm.value.content.trim()) { alert('请填写标题和内容'); return }
  try {
    await createForum({ title: postForm.value.title, categoryId: postForm.value.categoryId, content: postForm.value.content })
    showPostModal.value = false
    postForm.value = { title: '', categoryId: null, content: '' }
    searchForums()
    alert('发布成功')
  } catch (error) {
    alert('发布失败: ' + (error.message || '未知错误'))
  }
}

const searchForums = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getForums({ pageNum: 1, pageSize: 10, categoryId: currentCategory.value })
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
    const res = await getForums({ pageNum: pageNum.value, pageSize: 10, categoryId: currentCategory.value })
    forums.value = [...forums.value, ...(res.data.records || [])]
  } catch (error) {
    console.error('获取帖子列表失败:', error)
  }
  loading.value = false
}

const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

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
