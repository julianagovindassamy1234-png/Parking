<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { spotsApi } from '@/api/spots'
import type { ParkingSpot } from '@/types/parking'
import type { Page } from '@/types/requests'
import SpotStatusBadge from '@/components/parking/SpotStatusBadge.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseModal from '@/components/common/BaseModal.vue'
import BasePagination from '@/components/common/BasePagination.vue'

const page = ref<Page<ParkingSpot> | null>(null)
const showCreate = ref(false)
const form = ref({ spotCode: '', location: '', floor: '', spotType: 'STANDARD' })

onMounted(() => load(0))

async function load(p: number) {
  page.value = await spotsApi.list({ page: p })
}

async function create() {
  await spotsApi.create({
    spotCode: form.value.spotCode,
    location: form.value.location,
    floor: form.value.floor ? Number(form.value.floor) : null,
    spotType: form.value.spotType as ParkingSpot['spotType'],
  } as Omit<ParkingSpot, 'id' | 'isActive' | 'status'>)
  showCreate.value = false
  form.value = { spotCode: '', location: '', floor: '', spotType: 'STANDARD' }
  load(0)
}

async function remove(id: string) {
  if (!confirm('Deactivate this spot?')) return
  await spotsApi.delete(id)
  load(page.value?.number ?? 0)
}
</script>

<template>
  <div class="pt-14 space-y-4">
    <div class="flex items-center justify-between">
      <h1 class="text-xl font-bold text-gray-900">Spot Management</h1>
      <BaseButton @click="showCreate = true">+ Add Spot</BaseButton>
    </div>

    <div v-if="page" class="card overflow-x-auto">
      <table class="w-full text-sm">
        <thead>
          <tr class="border-b border-gray-100 text-gray-500 text-left">
            <th class="pb-3 font-medium">Code</th>
            <th class="pb-3 font-medium">Location</th>
            <th class="pb-3 font-medium">Floor</th>
            <th class="pb-3 font-medium">Type</th>
            <th class="pb-3 font-medium">Status</th>
            <th class="pb-3 font-medium"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="spot in page.content" :key="spot.id" class="border-b border-gray-50 hover:bg-gray-50">
            <td class="py-3 font-medium">{{ spot.spotCode }}</td>
            <td class="py-3 text-gray-500">{{ spot.location }}</td>
            <td class="py-3 text-gray-500">{{ spot.floor ?? '-' }}</td>
            <td class="py-3 text-gray-500 text-xs uppercase">{{ spot.spotType }}</td>
            <td class="py-3"><SpotStatusBadge :status="spot.status" /></td>
            <td class="py-3">
              <button class="text-xs text-red-500 hover:underline" @click="remove(spot.id)">Deactivate</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BasePagination
      v-if="page"
      :total-pages="page.totalPages"
      :current-page="page.number"
      @change="load"
    />

    <BaseModal v-if="showCreate" title="Add Parking Spot" @close="showCreate = false">
      <form class="space-y-3" @submit.prevent="create">
        <div>
          <label class="form-label">Spot Code</label>
          <input v-model="form.spotCode" class="form-input" required placeholder="A-01" />
        </div>
        <div>
          <label class="form-label">Location</label>
          <input v-model="form.location" class="form-input" required placeholder="Building A" />
        </div>
        <div>
          <label class="form-label">Floor</label>
          <input v-model="form.floor" type="number" class="form-input" placeholder="1" />
        </div>
        <div>
          <label class="form-label">Type</label>
          <select v-model="form.spotType" class="form-input">
            <option value="STANDARD">Standard</option>
            <option value="DISABLED">Disabled</option>
            <option value="ELECTRIC">Electric</option>
            <option value="MOTORCYCLE">Motorcycle</option>
          </select>
        </div>
        <BaseButton type="submit" class="w-full">Create</BaseButton>
      </form>
    </BaseModal>
  </div>
</template>
