import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import type { User, UserRole } from '@/types/auth'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const accessToken = ref<string | null>(null)
    const refreshToken = ref<string | null>(null)
    const user = ref<User | null>(null)

    const isAuthenticated = computed(() => !!accessToken.value)
    const userRole = computed<UserRole | null>(() => user.value?.role ?? null)
    const isAdmin = computed(() => user.value?.role === 'ADMIN')
    const isManagement = computed(() => user.value?.role === 'MANAGEMENT')
    const isEmployee = computed(() => user.value?.role === 'EMPLOYEE')

    async function login(email: string, password: string) {
      const data = await authApi.login(email, password)
      accessToken.value = data.accessToken
      refreshToken.value = data.refreshToken
      user.value = data.user
    }

    async function refresh() {
      if (!refreshToken.value) throw new Error('No refresh token')
      const data = await authApi.refresh(refreshToken.value)
      accessToken.value = data.accessToken
      refreshToken.value = data.refreshToken
      user.value = data.user
    }

    async function logout() {
      try { await authApi.logout() } catch { /* ignore */ }
      accessToken.value = null
      refreshToken.value = null
      user.value = null
    }

    return {
      accessToken,
      refreshToken,
      user,
      isAuthenticated,
      userRole,
      isAdmin,
      isManagement,
      isEmployee,
      login,
      refresh,
      logout,
    }
  },
  { persist: true },
)
