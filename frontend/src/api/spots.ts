import api from './axios'
import type { ParkingSpot } from '@/types/parking'
import type { Page } from '@/types/requests'

export interface SpotFilters {
  location?: string
  status?: string
  spotType?: string
  floor?: number
  page?: number
  size?: number
}

export const spotsApi = {
  list: (filters: SpotFilters = {}) =>
    api.get<Page<ParkingSpot>>('/spots', { params: filters }).then((r) => r.data),

  getById: (id: string) =>
    api.get<ParkingSpot>(`/spots/${id}`).then((r) => r.data),

  create: (data: Omit<ParkingSpot, 'id' | 'isActive' | 'status'>) =>
    api.post<ParkingSpot>('/spots', data).then((r) => r.data),

  update: (id: string, data: Partial<ParkingSpot>) =>
    api.put<ParkingSpot>(`/spots/${id}`, data).then((r) => r.data),

  delete: (id: string) => api.delete(`/spots/${id}`),
}
