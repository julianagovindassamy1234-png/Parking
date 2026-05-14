<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'secondary' | 'danger'
  loading?: boolean
  disabled?: boolean
}>()
</script>

<template>
  <button
    :class="[
      'btn',
      variant === 'danger' ? 'btn-danger' : variant === 'secondary' ? 'btn-secondary' : 'btn-primary',
    ]"
    :disabled="disabled || loading"
    v-bind="$attrs"
  >
    <span v-if="loading" class="mr-2 h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin inline-block" />
    <slot />
  </button>
</template>
