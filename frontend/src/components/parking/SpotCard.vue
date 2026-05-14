<script setup lang="ts">
import type { ParkingSpot } from '@/types/parking'
import SpotStatusBadge from './SpotStatusBadge.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps<{ spot: ParkingSpot }>()
const emit = defineEmits<{ request: [spotId: string] }>()
</script>

<template>
  <div class="card flex flex-col gap-3">
    <div class="flex items-start justify-between">
      <div>
        <p class="font-bold text-gray-900 text-lg">{{ spot.spotCode }}</p>
        <p class="text-sm text-gray-500">{{ spot.location }}<span v-if="spot.floor"> · Floor {{ spot.floor }}</span></p>
      </div>
      <SpotStatusBadge :status="spot.status" />
    </div>
    <p class="text-xs text-gray-400 uppercase tracking-wide">{{ spot.spotType }}</p>
    <BaseButton
      v-if="spot.status === 'AVAILABLE'"
      class="w-full mt-auto"
      @click="emit('request', spot.id)"
    >
      Request
    </BaseButton>
    <p v-else class="text-sm text-center text-gray-400 mt-auto">Unavailable</p>
  </div>
</template>
