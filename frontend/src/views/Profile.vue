<template>
  <div class="max-w-2xl mx-auto">
    <h2 class="text-2xl font-bold text-gray-800 mb-6">个人中心</h2>

    <div class="card mb-6">
      <h3 class="text-lg font-semibold text-gray-800 mb-4">基本信息</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-gray-500 mb-1 text-sm">用户名</label>
          <input :value="profile.username" class="input" disabled />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">角色</label>
          <input :value="roleText" class="input" disabled />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">邮箱</label>
          <input v-model="profile.email" class="input" :disabled="!editing" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">手机号</label>
          <input v-model="profile.phone" class="input" :disabled="!editing" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">真实姓名</label>
          <input v-model="profile.realName" class="input" :disabled="!editing" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">性别</label>
          <select v-model="profile.gender" class="input" :disabled="!editing">
            <option :value="0">未知</option>
            <option :value="1">男</option>
            <option :value="2">女</option>
          </select>
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">学校</label>
          <input v-model="profile.school" class="input" :disabled="!editing" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">专业</label>
          <input v-model="profile.major" class="input" :disabled="!editing" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">个人简介</label>
          <textarea v-model="profile.bio" class="input" rows="3" :disabled="!editing"></textarea>
        </div>
        <div class="flex space-x-4">
          <button v-if="!editing" @click="editing = true" class="btn btn-primary">编辑</button>
          <template v-else>
            <button @click="saveProfile" class="btn btn-primary">保存</button>
            <button @click="editing = false; loadProfile()" class="btn btn-secondary">取消</button>
          </template>
        </div>
      </div>
    </div>

    <div class="card">
      <h3 class="text-lg font-semibold text-gray-800 mb-4">修改密码</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-gray-500 mb-1 text-sm">原密码</label>
          <input v-model="passwordForm.oldPassword" type="password" class="input" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">新密码</label>
          <input v-model="passwordForm.newPassword" type="password" class="input" />
        </div>
        <div>
          <label class="block text-gray-500 mb-1 text-sm">确认新密码</label>
          <input v-model="passwordForm.confirmPassword" type="password" class="input" />
        </div>
        <button @click="changePassword" class="btn btn-primary">修改密码</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserProfile, updateUserProfile, updatePassword } from '@/api'

const profile = ref({
  username: '', email: '', phone: '', role: 1,
  realName: '', avatar: '', gender: 0, school: '', major: '', bio: ''
})
const editing = ref(false)
const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

const roleText = computed(() => {
  const map = { 1: '学生', 2: '教师', 3: '管理员' }
  return map[profile.value.role] || '未知'
})

const loadProfile = async () => {
  try {
    const res = await getUserProfile()
    if (res.code === 200) Object.assign(profile.value, res.data)
  } catch (error) {
    console.error('获取个人信息失败:', error)
  }
}

const saveProfile = async () => {
  try {
    const res = await updateUserProfile({
      email: profile.value.email, phone: profile.value.phone, realName: profile.value.realName,
      gender: profile.value.gender, school: profile.value.school, major: profile.value.major, bio: profile.value.bio
    })
    if (res.code === 200) { alert('保存成功'); editing.value = false }
    else alert(res.message || '保存失败')
  } catch (error) {
    alert('保存失败: ' + (error.message || '未知错误'))
  }
}

const changePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) { alert('两次输入的密码不一致'); return }
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) { alert('请填写完整'); return }
  try {
    const res = await updatePassword({ oldPassword: passwordForm.value.oldPassword, newPassword: passwordForm.value.newPassword })
    if (res.code === 200) { alert('密码修改成功'); passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' } }
    else alert(res.message || '修改失败')
  } catch (error) {
    alert('修改失败: ' + (error.message || '未知错误'))
  }
}

onMounted(loadProfile)
</script>
