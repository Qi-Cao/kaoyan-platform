import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Schools from '@/views/Schools.vue'
import SchoolDetail from '@/views/SchoolDetail.vue'
import Resources from '@/views/Resources.vue'
import ResourceDetail from '@/views/ResourceDetail.vue'
import Forums from '@/views/Forums.vue'
import ForumDetail from '@/views/ForumDetail.vue'
import Profile from '@/views/Profile.vue'
import AdminLayout from '@/views/admin/AdminLayout.vue'
import AdminDashboard from '@/views/admin/Dashboard.vue'
import AdminUsers from '@/views/admin/Users.vue'
import AdminTeachers from '@/views/admin/Teachers.vue'
import AdminResources from '@/views/admin/Resources.vue'
import AdminForums from '@/views/admin/Forums.vue'
import AdminSchools from '@/views/admin/Schools.vue'
import AdminNotices from '@/views/admin/Notices.vue'
import AdminComments from '@/views/admin/Comments.vue'
import AdminProfile from '@/views/admin/Profile.vue'

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
    path: '/resources/:id',
    name: 'ResourceDetail',
    component: ResourceDetail
  },
  {
    path: '/forums',
    name: 'Forums',
    component: Forums
  },
  {
    path: '/forums/:id',
    name: 'ForumDetail',
    component: ForumDetail
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiredRole: ['ADMIN', 'TEACHER'] },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: AdminDashboard
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: AdminUsers,
        meta: { adminOnly: true }
      },
      {
        path: 'teachers',
        name: 'AdminTeachers',
        component: AdminTeachers,
        meta: { adminOnly: true }
      },
      {
        path: 'resources',
        name: 'AdminResources',
        component: AdminResources
      },
      {
        path: 'forums',
        name: 'AdminForums',
        component: AdminForums
      },
      {
        path: 'schools',
        name: 'AdminSchools',
        component: AdminSchools
      },
      {
        path: 'notices',
        name: 'AdminNotices',
        component: AdminNotices
      },
      {
        path: 'comments',
        name: 'AdminComments',
        component: AdminComments
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: AdminProfile
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')
    return
  }
  
  if (to.matched.some(record => record.meta.requiredRole)) {
    const requiredRoles = to.matched.find(record => record.meta.requiredRole)?.meta.requiredRole
    if (requiredRoles && !requiredRoles.includes(authStore.userRole)) {
      next('/')
      return
    }
  }
  
  if (to.meta.adminOnly && authStore.userRole !== 'ADMIN') {
    next('/admin/dashboard')
    return
  }
  
  next()
})

export default router
