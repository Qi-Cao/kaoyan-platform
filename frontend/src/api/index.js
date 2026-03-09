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

// 认证
export const login = (username, password) => {
  return api.post('/auth/login', { username, password })
}

export const register = (data) => {
  return api.post('/auth/register', data)
}

export const getUserInfo = () => {
  return api.get('/auth/info')
}

// 院校
export const getSchools = (params) => {
  return api.get('/schools', { params })
}

export const getSchoolDetail = (id) => {
  return api.get(`/schools/${id}`)
}

export const getProvinces = () => {
  return api.get('/schools/provinces')
}

// 资源
export const getResources = (params) => {
  return api.get('/resources', { params })
}

export const getResourceDetail = (id) => {
  return api.get(`/resources/${id}`)
}

export const addResource = (data) => {
  return api.post('/resources', data)
}

// 论坛
export const getForums = (params) => {
  return api.get('/forums', { params })
}

export const getForumDetail = (id) => {
  return api.get(`/forums/${id}`)
}

export const createForum = (data) => {
  return api.post('/forums', data)
}

// 公告
export const getNotices = () => {
  return api.get('/notices')
}

// 分类
export const getCategories = () => {
  return api.get('/categories')
}

export const getForumCategories = () => {
  return api.get('/forum-categories')
}

export default api
