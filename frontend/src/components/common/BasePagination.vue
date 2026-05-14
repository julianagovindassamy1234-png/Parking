<script setup lang="ts">
const props = defineProps<{
  totalPages: number
  currentPage: number
}>()

const emit = defineEmits<{ change: [page: number] }>()

const pages = Array.from({ length: props.totalPages }, (_, i) => i)
</script>

<template>
  <div v-if="totalPages > 1" class="flex items-center gap-1 justify-center mt-4">
    <button
      class="px-3 py-1 text-sm rounded border border-gray-200 disabled:opacity-40"
      :disabled="currentPage === 0"
      @click="emit('change', currentPage - 1)"
    >
      Prev
    </button>
    <button
      v-for="p in pages"
      :key="p"
      class="px-3 py-1 text-sm rounded border"
      :class="p === currentPage ? 'bg-primary text-white border-primary' : 'border-gray-200 hover:bg-gray-50'"
      @click="emit('change', p)"
    >
      {{ p + 1 }}
    </button>
    <button
      class="px-3 py-1 text-sm rounded border border-gray-200 disabled:opacity-40"
      :disabled="currentPage === totalPages - 1"
      @click="emit('change', currentPage + 1)"
    >
      Next
    </button>
  </div>
</template>
