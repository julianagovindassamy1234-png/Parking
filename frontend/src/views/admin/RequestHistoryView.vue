<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { requestsApi } from '@/api/requests'
import type { ParkingRequest, Page } from '@/types/requests'
import RequestStatusBadge from '@/components/requests/RequestStatusBadge.vue'
import BasePagination from '@/components/common/BasePagination.vue'

const page = ref<Page<ParkingRequest> | null>(null)

onMounted(() => load(0))

async function load(p: number) {
  page.value = await requestsApi.all({ page: p })
}
</script>

<template>
  <div class="pt-14 space-y-4">
    <h1 class="text-xl font-bold text-gray-900">Request History</h1>

    <div v-if="page" class="card overflow-x-auto">
      <table class="w-full text-sm">
        <thead>
          <tr class="border-b border-gray-100 text-gray-500 text-left">
            <th class="pb-3 font-medium">Requester</th>
            <th class="pb-3 font-medium">Spot</th>
            <th class="pb-3 font-medium">Location</th>
            <th class="pb-3 font-medium">Status</th>
            <th class="pb-3 font-medium">Requested</th>
            <th class="pb-3 font-medium">Responded</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="req in page.content" :key="req.id" class="border-b border-gray-50 hover:bg-gray-50">
            <td class="py-3">{{ req.requesterName }}</td>
            <td class="py-3 font-medium">{{ req.spotCode }}</td>
            <td class="py-3 text-gray-500">{{ req.spotLocation }}</td>
            <td class="py-3"><RequestStatusBadge :status="req.status" /></td>
            <td class="py-3 text-gray-400 text-xs">{{ new Date(req.requestedAt).toLocaleString() }}</td>
            <td class="py-3 text-gray-400 text-xs">
              {{ req.respondedAt ? new Date(req.respondedAt).toLocaleString() : '-' }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BasePagination v-if="page" :total-pages="page.totalPages" :current-page="page.number" @change="load" />
  </div>
</template>
