<script setup lang="ts">
defineProps<{
  label: string
  value: number | string
  color?: 'green' | 'red' | 'yellow' | 'blue' | 'gray'
}>()
</script>

<template>
  <div class="card">
    <p class="text-sm font-medium text-gray-500">{{ label }}</p>
    <p
      class="text-3xl font-bold mt-1"
      :class="{
        'text-green-600': color === 'green',
        'text-red-600': color === 'red',
        'text-yellow-600': color === 'yellow',
        'text-blue-600': color === 'blue',
        'text-gray-700': !color || color === 'gray',
      }"
    >
      {{ value }}
    </p>
  </div>
</template>
