<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppNavbar from '@/components/layout/AppNavbar.vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const auth = useAuthStore()

const showLayout = computed(() => route.name !== 'Login' && auth.isAuthenticated)
</script>

<template>
  <div class="min-h-screen bg-gray-50 font-sans">
    <template v-if="showLayout">
      <AppNavbar />
      <div class="flex">
        <AppSidebar />
        <main class="flex-1 p-6 ml-64">
          <RouterView />
        </main>
      </div>
    </template>
    <template v-else>
      <RouterView />
    </template>
  </div>
</template>
