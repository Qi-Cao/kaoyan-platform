<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">资源管理</h2>

    <div class="flex gap-2 mb-6">
      <button @click="filterStatus = null; loadResources()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === null ? 'bg-primary-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-primary-300']">全部</button>
      <button @click="filterStatus = 0; loadResources()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === 0 ? 'bg-yellow-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-yellow-300']">待审核</button>
      <button @click="filterStatus = 1; loadResources()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === 1 ? 'bg-green-500 text-white' : 'bg-white text-gray-500 border border-gray-200 hover:border-green-300']">已发布</button>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">标题</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">分类</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">上传者</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">状态</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">浏览量</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">时间</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="r in resources" :key="r.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ r.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ r.title }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ r.category }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">用户#{{ r.userId }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="r.status === 1 ? 'text-green-500' : 'text-yellow-500'">{{ r.status === 1 ? '已发布' : '待审核' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ r.viewCount }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(r.createdAt) }}</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <a v-if="r.fileUrl" :href="r.fileUrl" target="_blank" download class="text-blue-500 hover:text-blue-600 transition-colors">下载</a>
              <span v-else class="text-gray-300">无文件</span>
              <button v-if="r.status === 0" @click="handleReview(r.id, 1)" class="text-green-500 hover:text-green-600 transition-colors">通过</button>
              <button v-if="r.status === 0" @click="handleReview(r.id, 0)" class="text-yellow-500 hover:text-yellow-600 transition-colors">拒绝</button>
              <button @click="handleDelete(r.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadResources())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadResources()" :disabled="resources.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminResources, reviewResource, deleteAdminResource } from '@/api'

const resources = ref([])
const pageNum = ref(1)
const total = ref(0)
const filterStatus = ref(null)
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadResources = async () => {
  try {
    const params = { pageNum: pageNum.value, pageSize: 10 }
    if (filterStatus.value !== null) params.status = filterStatus.value
    const res = await getAdminResources(params)
    if (res.code === 200) { resources.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取资源列表失败:', error) }
}

const handleReview = async (id, status) => {
  const reason = status === 0 ? prompt('请输入拒绝原因:') : ''
  if (status === 0 && reason === null) return
  try { await reviewResource(id, { status, reason: reason || '' }); loadResources() } catch (error) { alert('操作失败') }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该资源吗？')) return
  try { await deleteAdminResource(id); loadResources() } catch (error) { alert('删除失败') }
}

onMounted(loadResources)
</script>
