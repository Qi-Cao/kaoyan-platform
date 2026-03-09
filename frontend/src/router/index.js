import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Schools from '@/views/Schools.vue'
import SchoolDetail from '@/views/SchoolDetail.vue'
import Resources from '@/views/Resources.vue'
import Forums from '@/views/Forums.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/schools',
    name: 'Schools',
    component: Schools
  },
  {
    path: '/schools/:id',
    name: 'SchoolDetail',
    component: SchoolDetail
  },
  {
    path: '/resources',
    name: 'Resources',
    component: Resources
  },
  {
    path: '/forums',
    name: 'Forums',
    component: Forums
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
