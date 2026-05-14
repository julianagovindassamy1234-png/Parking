<script setup lang="ts">
import { onMounted } from 'vue'
import { useParkingStore } from '@/stores/parking'
import StatsCard from '@/components/dashboard/StatsCard.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import { useAuthStore } from '@/stores/auth'

const parking = useParkingStore()
const auth = useAuthStore()

onMounted(() => parking.fetchStats())
</script>

<template>
  <div class="pt-14">
    <h1 class="text-xl font-bold text-gray-900 mb-6">
      Welcome back, {{ auth.user?.firstName }}
    </h1>

    <LoadingSpinner v-if="!parking.stats" />

    <template v-else>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <StatsCard label="Total Spots" :value="parking.stats.totalSpots" />
        <StatsCard label="Available" :value="parking.stats.availableSpots" color="green" />
        <StatsCard label="Occupied" :value="parking.stats.occupiedSpots" color="red" />
        <StatsCard label="Pending Requests" :value="parking.stats.pendingRequests" color="yellow" />
      </div>

      <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
        <StatsCard label="Reserved" :value="parking.stats.reservedSpots" color="yellow" />
        <StatsCard label="Maintenance" :value="parking.stats.maintenanceSpots" color="gray" />
        <StatsCard label="Active Assignments" :value="parking.stats.activeAssignments" color="blue" />
      </div>
    </template>
  </div>
</template>
