import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import type { UserRole } from '@/types/auth'

declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
    requiredRoles?: UserRole[]
  }
}

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/',
      name: 'Dashboard',
      component: () => import('@/views/DashboardView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/parking',
      name: 'ParkingSearch',
      component: () => import('@/views/ParkingSearchView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/my-requests',
      name: 'MyRequests',
      component: () => import('@/views/MyRequestsView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['EMPLOYEE'] },
    },
    {
      path: '/approvals',
      name: 'Approvals',
      component: () => import('@/views/ApprovalsView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['MANAGEMENT', 'ADMIN'] },
    },
    {
      path: '/my-spot',
      name: 'MySpot',
      component: () => import('@/views/MySpotView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['EMPLOYEE'] },
    },
    {
      path: '/admin/spots',
      name: 'AdminSpots',
      component: () => import('@/views/admin/SpotManagementView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['ADMIN'] },
    },
    {
      path: '/admin/users',
      name: 'AdminUsers',
      component: () => import('@/views/admin/UserManagementView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['ADMIN'] },
    },
    {
      path: '/admin/requests',
      name: 'AdminRequests',
      component: () => import('@/views/admin/RequestHistoryView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['ADMIN'] },
    },
    {
      path: '/403',
      name: 'Forbidden',
      component: () => import('@/views/errors/ForbiddenView.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/errors/NotFoundView.vue'),
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }

  if (to.meta.requiredRoles && auth.userRole) {
    if (!to.meta.requiredRoles.includes(auth.userRole)) {
      return { name: 'Forbidden' }
    }
  }
})

export default router
