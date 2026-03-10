<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">论坛管理</h2>

    <div class="flex gap-2 mb-6">
      <button @click="filterStatus = null; loadForums()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === null ? 'bg-primary-500 text-white' : 'bg-white text-gray-500 border border-gray-200']">全部</button>
      <button @click="filterStatus = 1; loadForums()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === 1 ? 'bg-green-500 text-white' : 'bg-white text-gray-500 border border-gray-200']">正常</button>
      <button @click="filterStatus = 0; loadForums()" :class="['px-4 py-2 rounded-lg text-sm transition-all', filterStatus === 0 ? 'bg-red-500 text-white' : 'bg-white text-gray-500 border border-gray-200']">隐藏</button>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">标题</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">作者</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">浏览</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">回复</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">状态</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">时间</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="f in forums" :key="f.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ f.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ f.title }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">用户#{{ f.userId }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ f.viewCount }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ f.replyCount }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="f.status === 1 ? 'text-green-500' : 'text-red-500'">{{ f.status === 1 ? '正常' : '隐藏' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(f.createdAt) }}</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <button v-if="f.status === 1" @click="toggleStatus(f.id, 0)" class="text-yellow-500 hover:text-yellow-600 transition-colors">隐藏</button>
              <button v-else @click="toggleStatus(f.id, 1)" class="text-green-500 hover:text-green-600 transition-colors">恢复</button>
              <button @click="handleDelete(f.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadForums())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadForums()" :disabled="forums.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminForums, updateForumStatus, deleteAdminForum } from '@/api'

const forums = ref([])
const pageNum = ref(1)
const total = ref(0)
const filterStatus = ref(null)
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadForums = async () => {
  try {
    const params = { pageNum: pageNum.value, pageSize: 10 }
    if (filterStatus.value !== null) params.status = filterStatus.value
    const res = await getAdminForums(params)
    if (res.code === 200) { forums.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取论坛列表失败:', error) }
}

const toggleStatus = async (id, status) => {
  try { await updateForumStatus(id, status); loadForums() } catch (error) { alert('操作失败') }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该帖子吗？')) return
  try { await deleteAdminForum(id); loadForums() } catch (error) { alert('删除失败') }
}

onMounted(loadForums)
</script>
