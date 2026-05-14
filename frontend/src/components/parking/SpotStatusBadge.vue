<script setup lang="ts">
import type { SpotStatus } from '@/types/parking'
defineProps<{ status: SpotStatus }>()
</script>

<template>
  <span :class="`badge-${status.toLowerCase()}`">{{ status }}</span>
</template>
