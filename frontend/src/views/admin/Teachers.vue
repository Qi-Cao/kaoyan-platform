<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">教师管理</h2>
      <button @click="showAddModal = true" class="btn btn-primary">新增教师</button>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">用户名</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">邮箱</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">状态</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">注册时间</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="teacher in teachers" :key="teacher.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ teacher.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ teacher.username }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ teacher.email }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="teacher.status === 1 ? 'text-green-500' : 'text-red-500'">{{ teacher.status === 1 ? '正常' : '禁用' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(teacher.createdAt) }}</td>
            <td class="px-4 py-3 text-sm">
              <button @click="handleDelete(teacher.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadTeachers())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadTeachers()" :disabled="teachers.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>

    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-md mx-4">
        <h3 class="text-xl font-bold text-gray-800 mb-4">新增教师</h3>
        <div class="space-y-4">
          <div><label class="block text-gray-500 mb-1 text-sm">用户名</label><input v-model="addForm.username" class="input" placeholder="请输入用户名" /></div>
          <div><label class="block text-gray-500 mb-1 text-sm">密码</label><input v-model="addForm.password" type="password" class="input" placeholder="请输入密码" /></div>
          <div><label class="block text-gray-500 mb-1 text-sm">邮箱</label><input v-model="addForm.email" class="input" placeholder="请输入邮箱" /></div>
        </div>
        <div class="flex justify-end space-x-4 mt-6">
          <button @click="showAddModal = false" class="btn btn-secondary">取消</button>
          <button @click="submitAdd" class="btn btn-primary">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminTeachers, addTeacher, deleteTeacher } from '@/api'

const teachers = ref([])
const pageNum = ref(1)
const total = ref(0)
const showAddModal = ref(false)
const addForm = ref({ username: '', password: '', email: '' })
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadTeachers = async () => {
  try {
    const res = await getAdminTeachers({ pageNum: pageNum.value, pageSize: 10 })
    if (res.code === 200) { teachers.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取教师列表失败:', error) }
}

const submitAdd = async () => {
  if (!addForm.value.username || !addForm.value.password) { alert('请填写用户名和密码'); return }
  try {
    const res = await addTeacher(addForm.value)
    if (res.code === 200) { showAddModal.value = false; addForm.value = { username: '', password: '', email: '' }; loadTeachers() }
    else alert(res.message || '添加失败')
  } catch (error) { alert('添加失败: ' + (error.message || '未知错误')) }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该教师吗？')) return
  try { await deleteTeacher(id); loadTeachers() } catch (error) { alert('删除失败') }
}

onMounted(loadTeachers)
</script>
