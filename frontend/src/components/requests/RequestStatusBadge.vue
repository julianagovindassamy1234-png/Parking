<script setup lang="ts">
import type { RequestStatus } from '@/types/requests'
defineProps<{ status: RequestStatus }>()
</script>

<template>
  <span :class="`badge-${status.toLowerCase()}`">{{ status.replace('_', ' ') }}</span>
</template>
