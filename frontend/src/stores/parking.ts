import { defineStore } from 'pinia'
import { ref } from 'vue'
import { spotsApi, type SpotFilters } from '@/api/spots'
import { dashboardApi } from '@/api/dashboard'
import type { ParkingSpot, DashboardStats } from '@/types/parking'
import type { Page } from '@/types/requests'

export const useParkingStore = defineStore('parking', () => {
  const spots = ref<Page<ParkingSpot> | null>(null)
  const stats = ref<DashboardStats | null>(null)
  const loading = ref(false)

  async function fetchSpots(filters: SpotFilters = {}) {
    loading.value = true
    try {
      spots.value = await spotsApi.list(filters)
    } finally {
      loading.value = false
    }
  }

  async function fetchStats() {
    stats.value = await dashboardApi.stats()
  }

  return { spots, stats, loading, fetchSpots, fetchStats }
})
