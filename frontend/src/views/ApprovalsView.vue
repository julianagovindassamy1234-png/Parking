<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRequestsStore } from '@/stores/requests'
import CountdownTimer from '@/components/requests/CountdownTimer.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BasePagination from '@/components/common/BasePagination.vue'

const requests = useRequestsStore()
const rejectNotes = ref<Record<string, string>>({})

onMounted(() => requests.fetchPendingRequests())

async function approve(id: string) {
  await requests.approve(id)
}

async function reject(id: string) {
  await requests.reject(id, rejectNotes.value[id])
  delete rejectNotes.value[id]
}
</script>

<template>
  <div class="pt-14 space-y-6">
    <h1 class="text-xl font-bold text-gray-900">Pending Approvals</h1>

    <LoadingSpinner v-if="requests.loading" />

    <template v-else-if="requests.pendingRequests">
      <div v-if="requests.pendingRequests.content.length === 0" class="card text-center text-gray-400 py-10">
        No pending requests.
      </div>

      <div v-else class="space-y-4">
        <div
          v-for="req in requests.pendingRequests.content"
          :key="req.id"
          class="card flex flex-col sm:flex-row sm:items-center gap-4"
        >
          <div class="flex-1">
            <p class="font-semibold text-gray-900">{{ req.requesterName }}</p>
            <p class="text-sm text-gray-500">
              Spot <strong>{{ req.spotCode }}</strong> — {{ req.spotLocation }}
            </p>
            <p class="text-xs text-gray-400 mt-1">
              Requested {{ new Date(req.requestedAt).toLocaleString() }}
            </p>
          </div>
          <div class="text-center">
            <p class="text-xs text-gray-400 mb-1">Auto-approve in</p>
            <CountdownTimer :deadline="req.autoApproveDeadline" />
          </div>
          <div class="flex flex-col gap-2 min-w-[180px]">
            <BaseButton @click="approve(req.id)">Approve</BaseButton>
            <input
              v-model="rejectNotes[req.id]"
              class="form-input text-xs"
              placeholder="Rejection reason (optional)"
            />
            <BaseButton variant="danger" @click="reject(req.id)">Reject</BaseButton>
          </div>
        </div>
      </div>

      <BasePagination
        :total-pages="requests.pendingRequests.totalPages"
        :current-page="requests.pendingRequests.number"
        @change="requests.fetchPendingRequests($event)"
      />
    </template>
  </div>
</template>
