<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useParkingStore } from '@/stores/parking'
import { useRequestsStore } from '@/stores/requests'
import SpotFilterBar from '@/components/parking/SpotFilterBar.vue'
import SpotCard from '@/components/parking/SpotCard.vue'
import BasePagination from '@/components/common/BasePagination.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import type { SpotFilters } from '@/api/spots'

const parking = useParkingStore()
const requests = useRequestsStore()

const currentFilters = ref<SpotFilters>({})
const toast = ref('')

onMounted(() => parking.fetchSpots())

function onFilter(filters: SpotFilters) {
  currentFilters.value = { ...filters, page: 0 }
  parking.fetchSpots(currentFilters.value)
}

function onPageChange(page: number) {
  parking.fetchSpots({ ...currentFilters.value, page })
}

async function onRequest(spotId: string) {
  try {
    await requests.submitRequest(spotId)
    toast.value = 'Request submitted!'
    parking.fetchSpots(currentFilters.value)
    setTimeout(() => (toast.value = ''), 3000)
  } catch (e: unknown) {
    toast.value = 'Could not submit request. Spot may already be taken.'
    setTimeout(() => (toast.value = ''), 4000)
  }
}
</script>

<template>
  <div class="pt-14 space-y-6">
    <h1 class="text-xl font-bold text-gray-900">Find Parking</h1>

    <SpotFilterBar @filter="onFilter" />

    <div v-if="toast" class="rounded-lg px-4 py-2 text-sm font-medium" :class="toast.startsWith('Request') ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'">
      {{ toast }}
    </div>

    <LoadingSpinner v-if="parking.loading" />

    <template v-else-if="parking.spots">
      <div v-if="parking.spots.content.length === 0" class="text-center text-gray-400 py-16">
        No spots found.
      </div>
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        <SpotCard
          v-for="spot in parking.spots.content"
          :key="spot.id"
          :spot="spot"
          @request="onRequest"
        />
      </div>
      <BasePagination
        :total-pages="parking.spots.totalPages"
        :current-page="parking.spots.number"
        @change="onPageChange"
      />
    </template>
  </div>
</template>
