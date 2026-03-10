<template>
  <div>
    <router-link to="/schools" class="text-primary-500 hover:text-primary-600 transition-colors mb-4 inline-block">
      &larr; 返回院校库
    </router-link>
    
    <div v-if="school" class="card">
      <div class="flex items-start space-x-6">
        <div class="w-24 h-24 bg-primary-50 rounded-xl flex items-center justify-center text-4xl">
          🏫
        </div>
        <div class="flex-1">
          <h1 class="text-3xl font-bold text-gray-800">{{ school.name }}</h1>
          <p class="text-gray-400 mt-2">{{ school.province }} · {{ school.city }}</p>
          <div class="flex items-center mt-4 space-x-6">
            <div>
              <span class="text-gray-400 text-sm">排名</span>
              <p class="text-2xl font-bold text-primary-500">#{{ school.rank }}</p>
            </div>
            <div>
              <span class="text-gray-400 text-sm">录取率</span>
              <p class="text-2xl font-bold text-green-500">{{ school.admissionRate }}%</p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="mt-8">
        <h2 class="text-xl font-bold text-gray-800 mb-4">院校简介</h2>
        <p class="text-gray-500 leading-relaxed">{{ school.description || '暂无简介' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSchoolDetail } from '@/api'

const route = useRoute()
const school = ref(null)

onMounted(async () => {
  try {
    const res = await getSchoolDetail(route.params.id)
    school.value = res.data
  } catch (error) {
    console.error('获取院校详情失败:', error)
  }
})
</script>
