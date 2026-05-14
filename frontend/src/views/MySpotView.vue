<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { assignmentsApi } from '@/api/assignments'
import type { ParkingAssignment } from '@/types/parking'
import BaseButton from '@/components/common/BaseButton.vue'

const assignment = ref<ParkingAssignment | null>(null)
const loading = ref(true)
const error = ref('')
const releasing = ref(false)

onMounted(async () => {
  try {
    assignment.value = await assignmentsApi.my()
  } catch {
    error.value = 'No active assignment found.'
  } finally {
    loading.value = false
  }
})

async function release() {
  if (!assignment.value) return
  releasing.value = true
  try {
    assignment.value = await assignmentsApi.release(assignment.value.id)
  } finally {
    releasing.value = false
  }
}
</script>

<template>
  <div class="pt-14">
    <h1 class="text-xl font-bold text-gray-900 mb-6">My Spot</h1>

    <div v-if="loading" class="text-gray-400">Loading...</div>
    <div v-else-if="error" class="card text-center text-gray-400 py-10">{{ error }}</div>

    <div v-else-if="assignment" class="card max-w-sm space-y-4">
      <div>
        <p class="text-sm text-gray-500">Spot</p>
        <p class="text-2xl font-bold text-primary">{{ assignment.spotCode }}</p>
      </div>
      <div>
        <p class="text-sm text-gray-500">Location</p>
        <p class="font-medium">{{ assignment.spotLocation }}</p>
      </div>
      <div>
        <p class="text-sm text-gray-500">Assigned at</p>
        <p class="font-medium">{{ new Date(assignment.assignedAt).toLocaleString() }}</p>
      </div>
      <div v-if="!assignment.isActive" class="text-sm text-gray-400">Released</div>
      <BaseButton v-else variant="danger" :loading="releasing" @click="release">
        Release Spot
      </BaseButton>
    </div>
  </div>
</template>
