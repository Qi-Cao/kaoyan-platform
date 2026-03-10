<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">考研资源</h2>
      <button v-if="authStore.isLoggedIn" @click="showAddModal = true" class="btn btn-primary">上传资源</button>
    </div>

    <!-- 分类筛选 -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button v-for="cat in categories" :key="cat" 
              @click="currentCategory = cat; searchResources()"
              :class="['px-4 py-2 rounded-lg text-sm transition-all', 
                       currentCategory === cat ? 'bg-primary-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-primary-300 hover:text-primary-500']">
        {{ cat }}
      </button>
    </div>

    <!-- 资源列表 -->
    <div class="space-y-4">
      <div v-for="resource in resources" :key="resource.id" 
           class="card card-hover cursor-pointer"
           @click="$router.push('/resources/' + resource.id)">
        <div class="flex justify-between items-start">
          <div>
            <h3 class="font-semibold text-lg text-gray-800">{{ resource.title }}</h3>
            <p class="text-gray-400 text-sm mt-1">{{ resource.category }} &middot; {{ formatDate(resource.createdAt) }}</p>
          </div>
          <div class="flex items-center space-x-4 text-sm text-gray-400">
            <span>浏览 {{ resource.viewCount }}</span>
            <span>下载 {{ resource.downloadCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="resources.length === 0" class="text-center text-gray-400 py-16">暂无数据</div>

    <div class="flex justify-center mt-8">
      <button @click="loadMore" class="btn btn-secondary" :disabled="loading">
        {{ loading ? '加载中...' : '加载更多' }}
      </button>
    </div>

    <!-- 上传资源模态框 -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-lg mx-4">
        <h3 class="text-xl font-bold text-gray-800 mb-4">上传资源</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-gray-600 mb-1 text-sm">标题</label>
            <input v-model="addForm.title" class="input" placeholder="请输入资源标题" />
          </div>
          <div>
            <label class="block text-gray-600 mb-1 text-sm">分类</label>
            <select v-model="addForm.category" class="input">
              <option value="真题">真题</option>
              <option value="笔记">笔记</option>
              <option value="经验">经验</option>
              <option value="分数线">分数线</option>
            </select>
          </div>
          <div>
            <label class="block text-gray-600 mb-1 text-sm">内容描述</label>
            <textarea v-model="addForm.content" class="input" rows="4" placeholder="请输入资源描述"></textarea>
          </div>
          <div>
            <label class="block text-gray-600 mb-1 text-sm">上传文件</label>
            <input type="file" @change="handleFileChange" class="input text-sm" />
            <p v-if="uploadedUrl" class="text-sm text-green-500 mt-1">文件已上传</p>
          </div>
        </div>
        <div class="flex justify-end space-x-4 mt-6">
          <button @click="showAddModal = false; resetForm()" class="btn btn-secondary">取消</button>
          <button @click="submitResource" class="btn btn-primary" :disabled="uploading">
            {{ uploading ? '上传中...' : '提交' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getResources, addResource, uploadFile } from '@/api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const resources = ref([])
const categories = ['全部', '真题', '笔记', '经验', '分数线']
const currentCategory = ref('全部')
const pageNum = ref(1)
const loading = ref(false)
const showAddModal = ref(false)
const uploading = ref(false)
const uploadedUrl = ref('')

const addForm = ref({ title: '', category: '真题', content: '' })

const resetForm = () => {
  addForm.value = { title: '', category: '真题', content: '' }
  uploadedUrl.value = ''
}

const handleFileChange = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  uploading.value = true
  try {
    const res = await uploadFile(file)
    if (res.code === 200) {
      uploadedUrl.value = res.data.url
    } else {
      alert(res.message || '上传失败')
    }
  } catch (error) {
    alert('上传失败: ' + (error.message || '未知错误'))
  }
  uploading.value = false
}

const submitResource = async () => {
  if (!addForm.value.title.trim()) { alert('请输入标题'); return }
  try {
    await addResource({
      title: addForm.value.title,
      category: addForm.value.category,
      content: addForm.value.content,
      fileUrl: uploadedUrl.value
    })
    showAddModal.value = false
    resetForm()
    searchResources()
    alert('资源上传成功，待审核后发布')
  } catch (error) {
    alert('提交失败: ' + (error.message || '未知错误'))
  }
}

const searchResources = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getResources({ pageNum: 1, pageSize: 10, category: currentCategory.value === '全部' ? '' : currentCategory.value })
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
    const res = await getResources({ pageNum: pageNum.value, pageSize: 10, category: currentCategory.value === '全部' ? '' : currentCategory.value })
    resources.value = [...resources.value, ...(res.data.records || [])]
  } catch (error) {
    console.error('获取资源列表失败:', error)
  }
  loading.value = false
}

const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

onMounted(() => { searchResources() })
</script>
