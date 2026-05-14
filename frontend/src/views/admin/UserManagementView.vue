<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '@/api/axios'
import type { User } from '@/types/auth'
import type { Page } from '@/types/requests'
import BasePagination from '@/components/common/BasePagination.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseModal from '@/components/common/BaseModal.vue'

const page = ref<Page<User> | null>(null)
const showCreate = ref(false)
const form = ref({ email: '', password: '', firstName: '', lastName: '', role: 'EMPLOYEE', level: '' })

onMounted(() => load(0))

async function load(p: number) {
  page.value = await api.get<Page<User>>('/users', { params: { page: p } }).then((r) => r.data)
}

async function toggleActive(user: User) {
  const path = user.isActive ? 'deactivate' : 'activate'
  await api.put(`/users/${user.id}/${path}`)
  load(page.value?.number ?? 0)
}

async function create() {
  await api.post('/users', {
    ...form.value,
    level: form.value.level ? Number(form.value.level) : null,
  })
  showCreate.value = false
  load(0)
}
</script>

<template>
  <div class="pt-14 space-y-4">
    <div class="flex items-center justify-between">
      <h1 class="text-xl font-bold text-gray-900">User Management</h1>
      <BaseButton @click="showCreate = true">+ Add User</BaseButton>
    </div>

    <div v-if="page" class="card overflow-x-auto">
      <table class="w-full text-sm">
        <thead>
          <tr class="border-b border-gray-100 text-gray-500 text-left">
            <th class="pb-3 font-medium">Name</th>
            <th class="pb-3 font-medium">Email</th>
            <th class="pb-3 font-medium">Role</th>
            <th class="pb-3 font-medium">Level</th>
            <th class="pb-3 font-medium">Status</th>
            <th class="pb-3 font-medium"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in page.content" :key="user.id" class="border-b border-gray-50">
            <td class="py-3 font-medium">{{ user.firstName }} {{ user.lastName }}</td>
            <td class="py-3 text-gray-500">{{ user.email }}</td>
            <td class="py-3 text-xs uppercase">{{ user.role }}</td>
            <td class="py-3 text-gray-500">{{ user.level ?? '-' }}</td>
            <td class="py-3">
              <span :class="user.isActive ? 'text-green-600' : 'text-gray-400'" class="text-xs font-medium">
                {{ user.isActive ? 'Active' : 'Inactive' }}
              </span>
            </td>
            <td class="py-3">
              <button class="text-xs hover:underline" :class="user.isActive ? 'text-red-500' : 'text-green-600'" @click="toggleActive(user)">
                {{ user.isActive ? 'Deactivate' : 'Activate' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BasePagination v-if="page" :total-pages="page.totalPages" :current-page="page.number" @change="load" />

    <BaseModal v-if="showCreate" title="Add User" @close="showCreate = false">
      <form class="space-y-3" @submit.prevent="create">
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">First Name</label>
            <input v-model="form.firstName" class="form-input" required />
          </div>
          <div>
            <label class="form-label">Last Name</label>
            <input v-model="form.lastName" class="form-input" required />
          </div>
        </div>
        <div>
          <label class="form-label">Email</label>
          <input v-model="form.email" type="email" class="form-input" required />
        </div>
        <div>
          <label class="form-label">Password</label>
          <input v-model="form.password" type="password" class="form-input" required />
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Role</label>
            <select v-model="form.role" class="form-input">
              <option value="EMPLOYEE">Employee</option>
              <option value="MANAGEMENT">Management</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div>
            <label class="form-label">Level</label>
            <input v-model="form.level" type="number" class="form-input" placeholder="e.g. 12" />
          </div>
        </div>
        <BaseButton type="submit" class="w-full">Create User</BaseButton>
      </form>
    </BaseModal>
  </div>
</template>
