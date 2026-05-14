<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'

const props = defineProps<{ deadline: string }>()

const remaining = ref(0)
let timer: ReturnType<typeof setInterval>

function update() {
  remaining.value = Math.max(0, new Date(props.deadline).getTime() - Date.now())
}

onMounted(() => {
  update()
  timer = setInterval(update, 1000)
})

onUnmounted(() => clearInterval(timer))

const formatted = computed(() => {
  if (remaining.value <= 0) return 'Expired'
  const h = Math.floor(remaining.value / 3_600_000)
  const m = Math.floor((remaining.value % 3_600_000) / 60_000)
  const s = Math.floor((remaining.value % 60_000) / 1000)
  return `${h}h ${m}m ${s}s`
})
</script>

<template>
  <span :class="remaining <= 0 ? 'text-red-500' : 'text-yellow-600'" class="text-xs font-mono">
    {{ formatted }}
  </span>
</template>
