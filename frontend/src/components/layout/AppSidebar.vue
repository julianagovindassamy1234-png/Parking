<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
</script>

<template>
  <aside class="fixed left-0 top-14 bottom-0 w-64 bg-white border-r border-gray-200 flex flex-col py-4 overflow-y-auto z-20">
    <nav class="flex-1 px-3 space-y-1">
      <RouterLink
        to="/"
        class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
        active-class="bg-primary/10 text-primary"
      >
        Dashboard
      </RouterLink>

      <RouterLink
        to="/parking"
        class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
        active-class="bg-primary/10 text-primary"
      >
        Find Parking
      </RouterLink>

      <template v-if="auth.isEmployee">
        <RouterLink
          to="/my-requests"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          My Requests
        </RouterLink>

        <RouterLink
          to="/my-spot"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          My Spot
        </RouterLink>
      </template>

      <template v-if="auth.isManagement">
        <RouterLink
          to="/approvals"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          Pending Approvals
        </RouterLink>
      </template>

      <template v-if="auth.isAdmin">
        <div class="pt-3 pb-1 px-3">
          <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Admin</p>
        </div>
        <RouterLink
          to="/admin/spots"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          Spot Management
        </RouterLink>
        <RouterLink
          to="/admin/users"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          User Management
        </RouterLink>
        <RouterLink
          to="/admin/requests"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          Request History
        </RouterLink>
        <RouterLink
          to="/approvals"
          class="flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
          active-class="bg-primary/10 text-primary"
        >
          Pending Approvals
        </RouterLink>
      </template>
    </nav>
  </aside>
</template>
