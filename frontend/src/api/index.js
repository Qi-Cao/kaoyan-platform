import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

// ==================== 认证 ====================
export const login = (username, password) => {
  return api.post('/auth/login', { username, password })
}

export const register = (data) => {
  return api.post('/auth/register', data)
}

export const getUserInfo = () => {
  return api.get('/auth/info')
}

// ==================== 个人中心 ====================
export const getUserProfile = () => {
  return api.get('/user/profile')
}

export const updateUserProfile = (data) => {
  return api.put('/user/profile', data)
}

export const updatePassword = (data) => {
  return api.put('/user/password', data)
}

// ==================== 院校 ====================
export const getSchools = (params) => {
  return api.get('/schools', { params })
}

export const getSchoolDetail = (id) => {
  return api.get(`/schools/${id}`)
}

export const getProvinces = () => {
  return api.get('/schools/provinces')
}

// ==================== 资源 ====================
export const getResources = (params) => {
  return api.get('/resources', { params })
}

export const getResourceDetail = (id) => {
  return api.get(`/resources/${id}`)
}

export const addResource = (data) => {
  return api.post('/resources', data)
}

// ==================== 资源评论 ====================
export const getResourceComments = (params) => {
  return api.get('/resource-comments', { params })
}

export const addResourceComment = (data) => {
  return api.post('/resource-comments', data)
}

// ==================== 论坛 ====================
export const getForums = (params) => {
  return api.get('/forums', { params })
}

export const getForumDetail = (id) => {
  return api.get(`/forums/${id}`)
}

export const createForum = (data) => {
  return api.post('/forums', data)
}

// ==================== 论坛回复 ====================
export const getForumReplies = (params) => {
  return api.get('/forum-replies', { params })
}

export const addForumReply = (data) => {
  return api.post('/forum-replies', data)
}

// ==================== 公告 ====================
export const getNotices = () => {
  return api.get('/notices')
}

// ==================== 分类 ====================
export const getCategories = () => {
  return api.get('/categories')
}

export const getForumCategories = () => {
  return api.get('/forum-categories')
}

// ==================== 文件上传 ====================
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return api.post('/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 管理后台 ====================

// 数据统计
export const getAdminStats = () => {
  return api.get('/admin/stats')
}

// 用户管理
export const getAdminUsers = (params) => {
  return api.get('/admin/users', { params })
}

export const updateUserStatus = (id, status) => {
  return api.put(`/user/${id}/status`, { status })
}

export const deleteUser = (id) => {
  return api.delete(`/user/${id}`)
}

// 用户管理（教师/管理员）
export const getUserList = (params) => {
  return api.get('/user/list', { params })
}

export const getUserDetail = (id) => {
  return api.get(`/user/${id}`)
}

export const updateUser = (id, data) => {
  return api.put(`/user/${id}`, data)
}

export const resetUserPassword = (id) => {
  return api.put(`/user/${id}/reset-password`)
}

export const updateUserPassword = (id, newPassword) => {
  return api.put(`/user/${id}/password`, { newPassword })
}

// 教师管理
export const getAdminTeachers = (params) => {
  return api.get('/admin/teachers', { params })
}

export const addTeacher = (data) => {
  return api.post('/admin/teachers', data)
}

export const deleteTeacher = (id) => {
  return api.delete(`/admin/teachers/${id}`)
}

// 资源管理
export const getAdminResources = (params) => {
  return api.get('/admin/resources', { params })
}

export const reviewResource = (id, data) => {
  return api.put(`/admin/resources/${id}/review`, data)
}

export const deleteAdminResource = (id) => {
  return api.delete(`/admin/resources/${id}`)
}

// 论坛管理
export const getAdminForums = (params) => {
  return api.get('/admin/forums', { params })
}

export const updateForumStatus = (id, status) => {
  return api.put(`/admin/forums/${id}/status`, { status })
}

export const deleteAdminForum = (id) => {
  return api.delete(`/admin/forums/${id}`)
}

// 院校管理
export const adminAddSchool = (data) => {
  return api.post('/admin/schools', data)
}

export const adminUpdateSchool = (id, data) => {
  return api.put(`/admin/schools/${id}`, data)
}

export const adminDeleteSchool = (id) => {
  return api.delete(`/admin/schools/${id}`)
}

// 公告管理
export const getAdminNotices = (params) => {
  return api.get('/admin/notices', { params })
}

export const adminAddNotice = (data) => {
  return api.post('/admin/notices', data)
}

export const adminUpdateNotice = (id, data) => {
  return api.put(`/admin/notices/${id}`, data)
}

export const adminDeleteNotice = (id) => {
  return api.delete(`/admin/notices/${id}`)
}

// 资源留言
export const getAdminComments = (params) => {
  return api.get('/admin/comments', { params })
}

export default api
