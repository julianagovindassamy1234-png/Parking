<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

async function logout() {
  await auth.logout()
  router.push('/login')
}
</script>

<template>
  <header class="fixed top-0 left-0 right-0 z-30 h-14 bg-primary flex items-center justify-between px-6 shadow-md">
    <div class="flex items-center gap-3">
      <span class="text-white font-bold text-lg tracking-wide">Parking System</span>
    </div>
    <div class="flex items-center gap-4">
      <span class="text-white text-sm">
        {{ auth.user?.firstName }} {{ auth.user?.lastName }}
        <span class="ml-1 text-xs opacity-70">({{ auth.user?.role }})</span>
      </span>
      <button class="text-white text-sm hover:underline" @click="logout">Logout</button>
    </div>
  </header>
</template>
