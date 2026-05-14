<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import BaseButton from '@/components/common/BaseButton.vue'

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function submit() {
  error.value = ''
  loading.value = true
  try {
    await auth.login(email.value, password.value)
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (e: unknown) {
    error.value = 'Invalid email or password.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
    <div class="w-full max-w-sm">
      <div class="text-center mb-8">
        <h1 class="text-2xl font-bold text-primary">Parking System</h1>
        <p class="text-sm text-gray-500 mt-1">Sign in to your account</p>
      </div>

      <form class="card space-y-4" @submit.prevent="submit">
        <div>
          <label class="form-label" for="email">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            autocomplete="email"
            class="form-input"
            placeholder="you@company.com"
            required
          />
        </div>

        <div>
          <label class="form-label" for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            autocomplete="current-password"
            class="form-input"
            required
          />
        </div>

        <p v-if="error" class="text-sm text-red-500">{{ error }}</p>

        <BaseButton type="submit" class="w-full" :loading="loading">
          Sign in
        </BaseButton>
      </form>
    </div>
  </div>
</template>
