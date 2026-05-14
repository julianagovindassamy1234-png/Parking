import { defineStore } from 'pinia'
import { ref } from 'vue'
import { requestsApi } from '@/api/requests'
import type { ParkingRequest, Page } from '@/types/requests'

export const useRequestsStore = defineStore('requests', () => {
  const myRequests = ref<Page<ParkingRequest> | null>(null)
  const pendingRequests = ref<Page<ParkingRequest> | null>(null)
  const loading = ref(false)

  async function fetchMyRequests(page = 0) {
    loading.value = true
    try {
      myRequests.value = await requestsApi.my({ page })
    } finally {
      loading.value = false
    }
  }

  async function fetchPendingRequests(page = 0) {
    loading.value = true
    try {
      pendingRequests.value = await requestsApi.pending({ page })
    } finally {
      loading.value = false
    }
  }

  async function submitRequest(spotId: string) {
    return requestsApi.submit(spotId)
  }

  async function approve(id: string, notes?: string) {
    const updated = await requestsApi.approve(id, notes)
    refreshPending()
    return updated
  }

  async function reject(id: string, notes?: string) {
    const updated = await requestsApi.reject(id, notes)
    refreshPending()
    return updated
  }

  async function cancel(id: string) {
    const updated = await requestsApi.cancel(id)
    fetchMyRequests()
    return updated
  }

  function refreshPending() {
    fetchPendingRequests()
  }

  return {
    myRequests,
    pendingRequests,
    loading,
    fetchMyRequests,
    fetchPendingRequests,
    submitRequest,
    approve,
    reject,
    cancel,
  }
})
