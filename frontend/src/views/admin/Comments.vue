<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">资源留言</h2>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">资源ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">用户</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">内容</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">时间</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="c in comments" :key="c.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ c.id }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ c.resourceId }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">用户#{{ c.userId }}</td>
            <td class="px-4 py-3 text-sm text-gray-700">{{ c.content }}</td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(c.createdAt) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadComments())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadComments()" :disabled="comments.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminComments } from '@/api'

const comments = ref([])
const pageNum = ref(1)
const total = ref(0)
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadComments = async () => {
  try {
    const res = await getAdminComments({ pageNum: pageNum.value, pageSize: 10 })
    if (res.code === 200) { comments.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取留言列表失败:', error) }
}

onMounted(loadComments)
</script>
