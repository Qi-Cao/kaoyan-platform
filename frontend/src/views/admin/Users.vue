<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">用户管理</h2>

    <div class="flex gap-4 mb-6">
      <input v-model="searchUsername" @input="loadUsers()" placeholder="搜索用户名" class="input w-64" />
      <select v-model="filterRole" @change="loadUsers()" class="input w-40">
        <option :value="null">全部角色</option>
        <option :value="1">学生</option>
        <option :value="2">教师</option>
        <option :value="3">管理员</option>
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
            <td class="px-4 py-3 text-sm text-gray-500">{{ user.email || '-' }}</td>
            <td class="px-4 py-3 text-sm">
              <span :class="roleClass(user.role)">{{ roleText(user.role) }}</span>
            </td>
            <td class="px-4 py-3 text-sm">
              <span :class="user.status === 1 ? 'text-green-500' : 'text-red-500'">{{ user.status === 1 ? '正常' : '禁用' }}</span>
            </td>
            <td class="px-4 py-3 text-sm text-gray-400">{{ formatDate(user.createdAt) }}</td>
            <td class="px-4 py-3 text-sm space-x-2">
              <button @click="openEditDialog(user)" class="text-blue-500 hover:text-blue-600 transition-colors">编辑</button>
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

    <!-- 编辑用户弹窗 -->
    <div v-if="showEditDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl p-6 w-full max-w-lg max-h-[90vh] overflow-y-auto">
        <h3 class="text-xl font-bold text-gray-800 mb-4">编辑用户</h3>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
            <input v-model="editForm.username" disabled class="input bg-gray-100" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
            <input v-model="editForm.email" class="input" placeholder="请输入邮箱" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">手机号</label>
            <input v-model="editForm.phone" class="input" placeholder="请输入手机号" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">真实姓名</label>
            <input v-model="editForm.realName" class="input" placeholder="请输入真实姓名" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">性别</label>
            <select v-model="editForm.gender" class="input">
              <option :value="0">未知</option>
              <option :value="1">男</option>
              <option :value="2">女</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">毕业学校</label>
            <input v-model="editForm.school" class="input" placeholder="请输入毕业学校" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">专业</label>
            <input v-model="editForm.major" class="input" placeholder="请输入专业" />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">个人简介</label>
            <textarea v-model="editForm.bio" class="input" rows="3" placeholder="请输入个人简介"></textarea>
          </div>

          <!-- 管理员才能修改的字段 -->
          <template v-if="isAdmin">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">角色</label>
              <select v-model="editForm.role" class="input">
                <option :value="1">学生</option>
                <option :value="2">教师</option>
                <option :value="3">管理员</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">状态</label>
              <select v-model="editForm.status" class="input">
                <option :value="1">正常</option>
                <option :value="0">禁用</option>
              </select>
            </div>
          </template>

          <!-- 密码操作 -->
          <div class="border-t pt-4 mt-4">
            <h4 class="font-medium text-gray-800 mb-3">密码操作</h4>

            <template v-if="isAdmin">
              <button @click="handleResetPassword" class="btn btn-secondary w-full mb-2">重置密码为 123456</button>
            </template>

            <button @click="showPasswordDialog = true" class="btn btn-primary w-full">修改用户密码</button>
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeEditDialog" class="btn btn-secondary">取消</button>
          <button @click="handleUpdate" class="btn btn-primary">保存</button>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <div v-if="showPasswordDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl p-6 w-full max-w-md">
        <h3 class="text-xl font-bold text-gray-800 mb-4">修改密码</h3>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">新密码</label>
            <input v-model="passwordForm.newPassword" type="password" class="input" placeholder="请输入新密码" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">确认密码</label>
            <input v-model="passwordForm.confirmPassword" type="password" class="input" placeholder="请确认新密码" />
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="showPasswordDialog = false" class="btn btn-secondary">取消</button>
          <button @click="handleUpdatePassword" class="btn btn-primary">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserList, getUserDetail, updateUser, resetUserPassword, updateUserPassword, updateUserStatus, deleteUser } from '@/api'

const users = ref([])
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)
const filterRole = ref(null)
const searchUsername = ref('')
const currentUserRole = ref(3) // 假设当前用户是管理员

const isAdmin = computed(() => currentUserRole.value === 3)

// 编辑弹窗
const showEditDialog = ref(false)
const showPasswordDialog = ref(false)
const editingUserId = ref(null)
const editForm = ref({
  username: '',
  email: '',
  phone: '',
  realName: '',
  gender: 0,
  school: '',
  major: '',
  bio: '',
  role: 1,
  status: 1
})

const passwordForm = ref({
  newPassword: '',
  confirmPassword: ''
})

const roleText = (role) => ({ 1: '学生', 2: '教师', 3: '管理员' }[role] || '未知')
const roleClass = (role) => ({ 1: 'text-blue-500', 2: 'text-green-500', 3: 'text-purple-500' }[role] || 'text-gray-400')
const formatDate = (date) => new Date(date).toLocaleDateString('zh-CN')

const loadUsers = async () => {
  try {
    const params = { pageNum: pageNum.value, pageSize }
    if (filterRole.value !== null) params.role = filterRole.value
    if (searchUsername.value) params.username = searchUsername.value
    const res = await getUserList(params)
    if (res.code === 200) { users.value = res.data.records || []; total.value = res.data.total || 0 }
  } catch (error) { console.error('获取用户列表失败:', error) }
}

const openEditDialog = async (user) => {
  editingUserId.value = user.id
  try {
    const res = await getUserDetail(user.id)
    if (res.code === 200) {
      const data = res.data
      editForm.value = {
        username: data.username || '',
        email: data.email || '',
        phone: data.phone || '',
        realName: data.realName || '',
        gender: data.gender || 0,
        school: data.school || '',
        major: data.major || '',
        bio: data.bio || '',
        role: data.role || 1,
        status: data.status || 1
      }
    }
  } catch (error) {
    console.error('获取用户详情失败:', error)
  }
  showEditDialog.value = true
}

const closeEditDialog = () => {
  showEditDialog.value = false
  showPasswordDialog.value = false
  editingUserId.value = null
  passwordForm.value = { newPassword: '', confirmPassword: '' }
}

const handleUpdate = async () => {
  try {
    const res = await updateUser(editingUserId.value, editForm.value)
    if (res.code === 200) {
      alert('更新成功')
      closeEditDialog()
      loadUsers()
    } else {
      alert(res.message || '更新失败')
    }
  } catch (error) {
    alert('更新失败')
  }
}

const handleResetPassword = async () => {
  if (!confirm('确定要重置该用户密码吗？重置后密码将变为 123456')) return
  try {
    const res = await resetUserPassword(editingUserId.value)
    if (res.code === 200) {
      alert('密码已重置为: 123456')
    } else {
      alert(res.message || '重置失败')
    }
  } catch (error) {
    alert('重置失败')
  }
}

const handleUpdatePassword = async () => {
  if (!passwordForm.value.newPassword) {
    alert('请输入新密码')
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('两次密码输入不一致')
    return
  }
  try {
    const res = await updateUserPassword(editingUserId.value, passwordForm.value.newPassword)
    if (res.code === 200) {
      alert('密码修改成功')
      closeEditDialog()
    } else {
      alert(res.message || '修改失败')
    }
  } catch (error) {
    alert('修改失败')
  }
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
