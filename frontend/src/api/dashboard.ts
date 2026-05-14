import api from './axios'
import type { DashboardStats } from '@/types/parking'

export const dashboardApi = {
  stats: () => api.get<DashboardStats>('/dashboard/stats').then((r) => r.data),
}
