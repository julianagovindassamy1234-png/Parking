import api from './axios'
import type { ParkingAssignment } from '@/types/parking'
import type { Page } from '@/types/requests'

export const assignmentsApi = {
  my: () => api.get<ParkingAssignment>('/assignments/my').then((r) => r.data),

  release: (id: string) =>
    api.put<ParkingAssignment>(`/assignments/${id}/release`).then((r) => r.data),

  all: (params = {}) =>
    api.get<Page<ParkingAssignment>>('/assignments', { params }).then((r) => r.data),
}
