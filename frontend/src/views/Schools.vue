<template>
  <div>
    <h2 class="text-2xl font-bold text-gray-800 mb-6">院校库</h2>
    
    <!-- 搜索筛选 -->
    <div class="card mb-6">
      <div class="flex flex-wrap gap-4">
        <input v-model="keyword" type="text" class="input flex-1" placeholder="搜索院校名称..." />
        <select v-model="province" class="input w-40">
          <option value="">全部省份</option>
          <option v-for="p in provinces" :key="p" :value="p">{{ p }}</option>
        </select>
        <button @click="searchSchools" class="btn btn-primary">
          搜索
        </button>
      </div>
    </div>

    <!-- 院校列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <router-link v-for="school in schools" :key="school.id" 
                   :to="`/schools/${school.id}`"
                   class="card hover:shadow-md transition-shadow">
        <div class="flex items-start space-x-4">
          <div class="w-16 h-16 bg-primary-100 rounded-lg flex items-center justify-center text-2xl">
            🏫
          </div>
          <div class="flex-1">
            <h3 class="font-semibold text-lg text-gray-800">{{ school.name }}</h3>
            <p class="text-gray-500 text-sm mt-1">{{ school.province }} · {{ school.city }}</p>
            <div class="flex items-center mt-2 text-sm">
              <span class="text-primary-600 font-medium">排名: {{ school.rank }}</span>
              <span class="mx-2 text-gray-300">|</span>
              <span class="text-green-600">录取率: {{ school.admissionRate }}%</span>
            </div>
          </div>
        </div>
      </router-link>
    </div>

    <!-- 分页 -->
    <div class="flex justify-center mt-8">
      <button @click="loadMore" class="btn btn-secondary" :disabled="loading">
        {{ loading ? '加载中...' : '加载更多' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSchools, getProvinces } from '@/api'

const schools = ref([])
const provinces = ref([])
const keyword = ref('')
const province = ref('')
const pageNum = ref(1)
const loading = ref(false)

const searchSchools = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getSchools({ pageNum: 1, pageSize: 12, keyword: keyword.value, province: province.value })
    schools.value = res.data.records || []
  } catch (error) {
    console.error('获取院校列表失败:', error)
  }
  loading.value = false
}

const loadMore = async () => {
  pageNum.value++
  loading.value = true
  try {
    const res = await getSchools({ pageNum: pageNum.value, pageSize: 12, keyword: keyword.value, province: province.value })
    schools.value = [...schools.value, ...(res.data.records || [])]
  } catch (error) {
    console.error('获取院校列表失败:', error)
  }
  loading.value = false
}

onMounted(async () => {
  searchSchools()
  try {
    const res = await getProvinces()
    provinces.value = res.data || []
  } catch (error) {
    console.error('获取省份失败:', error)
  }
})
</script>
