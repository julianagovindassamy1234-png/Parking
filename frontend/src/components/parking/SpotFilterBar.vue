<script setup lang="ts">
import { reactive } from 'vue'
import type { SpotFilters } from '@/api/spots'

const emit = defineEmits<{ filter: [filters: SpotFilters] }>()

const filters = reactive<SpotFilters>({
  location: '',
  status: undefined,
  spotType: undefined,
  floor: undefined,
})

function apply() {
  emit('filter', { ...filters })
}

function reset() {
  filters.location = ''
  filters.status = undefined
  filters.spotType = undefined
  filters.floor = undefined
  emit('filter', {})
}
</script>

<template>
  <div class="card flex flex-wrap gap-3 items-end">
    <div>
      <label class="form-label">Location</label>
      <input v-model="filters.location" class="form-input w-48" placeholder="Building / Zone" />
    </div>
    <div>
      <label class="form-label">Status</label>
      <select v-model="filters.status" class="form-input w-40">
        <option value="">All</option>
        <option value="AVAILABLE">Available</option>
        <option value="OCCUPIED">Occupied</option>
        <option value="RESERVED">Reserved</option>
        <option value="MAINTENANCE">Maintenance</option>
      </select>
    </div>
    <div>
      <label class="form-label">Type</label>
      <select v-model="filters.spotType" class="form-input w-36">
        <option value="">All</option>
        <option value="STANDARD">Standard</option>
        <option value="DISABLED">Disabled</option>
        <option value="ELECTRIC">Electric</option>
        <option value="MOTORCYCLE">Motorcycle</option>
      </select>
    </div>
    <div>
      <label class="form-label">Floor</label>
      <input v-model.number="filters.floor" type="number" class="form-input w-24" placeholder="Any" />
    </div>
    <button class="btn-primary" @click="apply">Search</button>
    <button class="btn-secondary" @click="reset">Reset</button>
  </div>
</template>
