<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold text-gray-800">院校管理</h2>
      <button @click="openAdd" class="btn btn-primary">新增院校</button>
    </div>

    <div class="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">ID</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">名称</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">省份</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">城市</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">排名</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">录取率</th>
            <th class="px-4 py-3 text-left text-sm font-medium text-gray-500">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="s in schools" :key="s.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-4 py-3 text-sm text-gray-600">{{ s.id }}</td>
            <td class="px-4 py-3 text-sm font-medium text-gray-800">{{ s.name }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ s.province }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ s.city }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ s.rank }}</td>
            <td class="px-4 py-3 text-sm text-gray-500">{{ s.admissionRate }}%</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <button @click="openEdit(s)" class="text-blue-500 hover:text-blue-600 transition-colors">编辑</button>
              <button @click="handleDelete(s.id)" class="text-red-500 hover:text-red-600 transition-colors">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="flex justify-between items-center mt-4">
      <span class="text-sm text-gray-400">共 {{ total }} 条</span>
      <div class="flex gap-2">
        <button @click="pageNum > 1 && (pageNum--, loadSchools())" :disabled="pageNum <= 1" class="btn btn-secondary text-sm">上一页</button>
        <span class="px-3 py-1 text-sm text-gray-500">{{ pageNum }}</span>
        <button @click="pageNum++; loadSchools()" :disabled="schools.length < 10" class="btn btn-secondary text-sm">下一页</button>
      </div>
    </div>

    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-lg mx-4">
        <h3 class="text-xl font-bold text-gray-800 mb-4">{{ editingId ? '编辑院校' : '新增院校' }}</h3>
        <div class="space-y-4">
          <div><label class="block text-gray-500 mb-1 text-sm">名称</label><input v-model="form.name" class="input" /></div>
          <div class="grid grid-cols-2 gap-4">
            <div><label class="block text-gray-500 mb-1 text-sm">省份</label><input v-model="form.province" class="input" /></div>
            <div><label class="block text-gray-500 mb-1 text-sm">城市</label><input v-model="form.city" class="input" /></div>
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div><label class="block text-gray-500 mb-1 text-sm">排名</label><input v-model.number="form.rank" type="number" class="input" /></div>
            <div><label class="block text-gray-500 mb-1 text-sm">录取率(%)</label><input v-model.number="form.admissionRate" type="number" step="0.01" class="input" /></div>
          </div>
          <div><label class="block text-gray-500 mb-1 text-sm">简介</label><textarea v-model="form.description" class="input" rows="3"></textarea></div>
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
import { getSchools, adminAddSchool, adminUpdateSchool, adminDeleteSchool } from '@/api'

const schools = ref([])
const pageNum = ref(1)
const total = ref(0)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({ name: '', province: '', city: '', rank: null, admissionRate: null, description: '' })

const loadSchools = async () => {
  try {
    const res = await getSchools({ pageNum: pageNum.value, pageSize: 10 })
    if (res.code === 200) { schools.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取院校列表失败:', error) }
}

const openAdd = () => { editingId.value = null; form.value = { name: '', province: '', city: '', rank: null, admissionRate: null, description: '' }; showModal.value = true }
const openEdit = (school) => { editingId.value = school.id; form.value = { ...school }; showModal.value = true }

const submitForm = async () => {
  if (!form.value.name) { alert('请输入名称'); return }
  try {
    if (editingId.value) await adminUpdateSchool(editingId.value, form.value)
    else await adminAddSchool(form.value)
    showModal.value = false; loadSchools()
  } catch (error) { alert('操作失败: ' + (error.message || '未知错误')) }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除该院校吗？')) return
  try { await adminDeleteSchool(id); loadSchools() } catch (error) { alert('删除失败') }
}

onMounted(loadSchools)
</script>
