<script setup lang="ts">
import { onMounted } from 'vue'
import { useRequestsStore } from '@/stores/requests'
import RequestStatusBadge from '@/components/requests/RequestStatusBadge.vue'
import BasePagination from '@/components/common/BasePagination.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'

const requests = useRequestsStore()

onMounted(() => requests.fetchMyRequests())
</script>

<template>
  <div class="pt-14 space-y-6">
    <h1 class="text-xl font-bold text-gray-900">My Requests</h1>

    <LoadingSpinner v-if="requests.loading" />

    <template v-else-if="requests.myRequests">
      <div v-if="requests.myRequests.content.length === 0" class="card text-center text-gray-400 py-10">
        No requests yet.
      </div>

      <div v-else class="card overflow-x-auto">
        <table class="w-full text-sm">
          <thead>
            <tr class="border-b border-gray-100 text-gray-500 text-left">
              <th class="pb-3 font-medium">Spot</th>
              <th class="pb-3 font-medium">Location</th>
              <th class="pb-3 font-medium">Status</th>
              <th class="pb-3 font-medium">Requested</th>
              <th class="pb-3 font-medium">Auto-approve</th>
              <th class="pb-3 font-medium"></th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="req in requests.myRequests.content"
              :key="req.id"
              class="border-b border-gray-50 hover:bg-gray-50"
            >
              <td class="py-3 font-medium">{{ req.spotCode }}</td>
              <td class="py-3 text-gray-500">{{ req.spotLocation }}</td>
              <td class="py-3"><RequestStatusBadge :status="req.status" /></td>
              <td class="py-3 text-gray-500">{{ new Date(req.requestedAt).toLocaleDateString() }}</td>
              <td class="py-3 text-gray-500 text-xs">{{ new Date(req.autoApproveDeadline).toLocaleString() }}</td>
              <td class="py-3">
                <button
                  v-if="req.status === 'PENDING'"
                  class="text-xs text-red-500 hover:underline"
                  @click="requests.cancel(req.id)"
                >
                  Cancel
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <BasePagination
        :total-pages="requests.myRequests.totalPages"
        :current-page="requests.myRequests.number"
        @change="requests.fetchMyRequests($event)"
      />
    </template>
  </div>
</template>
