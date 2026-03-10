<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">公告管理</h2>
      <button @click="openAdd" class="btn btn-primary">新增公告</button>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">标题</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">置顶</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">发布时间</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="n in notices" :key="n.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ n.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ n.title }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="n.isTop ? 'text-primary-500' : 'text-gray-300'">{{ n.isTop ? '是' : '否' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(n.createdAt) }}</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <button @click="openEdit(n)" class="text-blue-500 hover:text-blue-600 transition-colors">编辑</button>
              <button @click="handleDelete(n.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadNotices())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadNotices()" :disabled="notices.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>

    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-lg mx-4">
        <h3 class="text-xl font-bold text-gray-800 mb-4">{{ editingId ? '编辑公告' : '新增公告' }}</h3>
        <div class="space-y-4">
          <div><label class="block text-gray-500 mb-1 text-sm">标题</label><input v-model="form.title" class="input" /></div>
          <div><label class="block text-gray-500 mb-1 text-sm">内容</label><textarea v-model="form.content" class="input" rows="6"></textarea></div>
          <div class="flex items-center">
            <input v-model="form.isTop" type="checkbox" :true-value="1" :false-value="0" class="mr-2 w-4 h-4 accent-primary-500" />
            <label class="text-gray-600 text-sm">置顶</label>
          </div>
        </div>
        <div class="flex justify-end space-x-4 mt-6">
          <button @click="showModal = false" class="btn btn-secondary">取消</button>
          <button @click="submitForm" class="btn btn-primary">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminNotices, adminAddNotice, adminUpdateNotice, adminDeleteNotice } from '@/api'

const notices = ref([])
const pageNum = ref(1)
const total = ref(0)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({ title: '', content: '', isTop: 0 })
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadNotices = async () => {
  try {
    const res = await getAdminNotices({ pageNum: pageNum.value, pageSize: 10 })
    if (res.code === 200) { notices.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取公告列表失败:', error) }
}

const openAdd = () => { editingId.value = null; form.value = { title: '', content: '', isTop: 0 }; showModal.value = true }
const openEdit = (notice) => { editingId.value = notice.id; form.value = { title: notice.title, content: notice.content, isTop: notice.isTop || 0 }; showModal.value = true }

const submitForm = async () => {
  if (!form.value.title || !form.value.content) { alert('请填写标题和内容'); return }
  try {
    if (editingId.value) await adminUpdateNotice(editingId.value, form.value)
    else await adminAddNotice(form.value)
    showModal.value = false; loadNotices()
  } catch (error) { alert('操作失败: ' + (error.message || '未知错误')) }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该公告吗？')) return
  try { await adminDeleteNotice(id); loadNotices() } catch (error) { alert('删除失败') }
}

onMounted(loadNotices)
</script>
