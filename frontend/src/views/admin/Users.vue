<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">用户管理</h2>

    <div class="flex gap-4 mb-6">
      <select v-model="filterRole" @change="loadUsers()" class="input w-40">
        <option :value="null">全部角色</option>
        <option :value="1">学生</option>
        <option :value="2">教师</option>
        <option :value="3">管理员</option>
      </select>
      <select v-model="filterStatus" @change="loadUsers()" class="input w-40">
        <option :value="null">全部状态</option>
        <option :value="1">正常</option>
        <option :value="0">禁用</option>
      </select>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">用户名</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">邮箱</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">角色</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">状态</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">注册时间</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="user in users" :key="user.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ user.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ user.username }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ user.email }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="roleClass(user.role)">{{ roleText(user.role) }}</span>
            </td>
            <td class="px-4 py-3 text-sm">
              <span :class="user.status === 1 ? 'text-green-500' : 'text-red-500'">{{ user.status === 1 ? '正常' : '禁用' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(user.createdAt) }}</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <button v-if="user.status === 1" @click="toggleStatus(user.id, 0)" class="text-yellow-500 hover:text-yellow-600 transition-colors">禁用</button>
              <button v-else @click="toggleStatus(user.id, 1)" class="text-green-500 hover:text-green-600 transition-colors">启用</button>
              <button @click="handleDelete(user.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadUsers())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadUsers()" :disabled="users.length < pageSize" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminUsers, updateUserStatus, deleteUser } from '@/api'

const users = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const filterRole = ref(null)
const filterStatus = ref(null)

const roleText = (role) => ({ 1: '学生', 2: '教师', 3: '管理员' }[role] || '未知')
const roleClass = (role) => ({ 1: 'text-blue-500', 2: 'text-green-500', 3: 'text-purple-500' }[role] || 'text-gray-400')
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadUsers = async () => {
  try {
    const params = { pageNum: pageNum.value, pageSize }
    if (filterRole.value !== null) params.role = filterRole.value
    if (filterStatus.value !== null) params.status = filterStatus.value
    const res = await getAdminUsers(params)
    if (res.code === 200) { users.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取用户列表失败:', error) }
}

const toggleStatus = async (id, status) => {
  try { await updateUserStatus(id, status); loadUsers() } catch (error) { alert('操作失败') }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该用户吗？')) return
  try { await deleteUser(id); loadUsers() } catch (error) { alert('删除失败') }
}

onMounted(loadUsers)
</script>
