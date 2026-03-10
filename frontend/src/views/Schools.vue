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
        <button @click="searchSchools" class="btn btn-primary">搜索</button>
      </div>
    </div>

    <!-- 院校列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <router-link v-for="school in schools" :key="school.id" 
                   :to="`/schools/${school.id}`"
                   class="card card-hover group">
        <div class="flex items-start space-x-4">
          <div class="w-16 h-16 bg-primary-50 rounded-lg flex items-center justify-center text-2xl group-hover:bg-primary-100 transition-colors">
            🏫
          </div>
          <div class="flex-1">
            <h3 class="font-semibold text-lg text-gray-800 group-hover:text-primary-500 transition-colors">{{ school.name }}</h3>
            <p class="text-gray-400 text-sm mt-1">{{ school.province }} · {{ school.city }}</p>
            <div class="flex items-center mt-2 text-sm">
              <span class="text-primary-500 font-medium">排名: {{ school.rank }}</span>
              <span class="mx-2 text-gray-200">|</span>
              <span class="text-green-500">录取率: {{ school.admissionRate }}%</span>
            </div>
          </div>
        </div>
      </router-link>
    </div>

    <div v-if="schools.length === 0" class="text-center text-gray-400 py-16">暂无数据</div>

    <div v-if="hasMore" class="flex justify-center mt-8">
      <button @click="loadMore" class="btn btn-secondary" :disabled="loading">
        {{ loading ? '加载中...' : '加载更多' }}
      </button>
    </div>
    <div v-else-if="schools.length > 0" class="text-center text-gray-400 py-6 text-sm">已加载全部院校</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getSchools, getProvinces } from '@/api'

const schools = ref([])
const provinces = ref([])
const keyword = ref('')
const province = ref('')
const pageNum = ref(1)
const total = ref(0)
const loading = ref(false)
const hasMore = computed(() => schools.value.length < total.value)

const searchSchools = async () => {
  pageNum.value = 1
  loading.value = true
  try {
    const res = await getSchools({ pageNum: 1, pageSize: 12, keyword: keyword.value, province: province.value })
    schools.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取院校列表失败:', error)
  }
  loading.value = false
}

const loadMore = async () => {
  if (!hasMore.value) return
  pageNum.value++
  loading.value = true
  try {
    const res = await getSchools({ pageNum: pageNum.value, pageSize: 12, keyword: keyword.value, province: province.value })
    const newRecords = res.data.records || []
    schools.value = [...schools.value, ...newRecords]
    total.value = res.data.total || 0
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
