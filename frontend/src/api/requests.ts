import api from './axios'
import type { ParkingRequest, Page } from '@/types/requests'

export const requestsApi = {
  submit: (spotId: string) =>
    api.post<ParkingRequest>('/requests', { spotId }).then((r) => r.data),

  my: (params = {}) =>
    api.get<Page<ParkingRequest>>('/requests/my', { params }).then((r) => r.data),

  pending: (params = {}) =>
    api.get<Page<ParkingRequest>>('/requests/pending', { params }).then((r) => r.data),

  getById: (id: string) =>
    api.get<ParkingRequest>(`/requests/${id}`).then((r) => r.data),

  approve: (id: string, notes?: string) =>
    api.put<ParkingRequest>(`/requests/${id}/approve`, { notes }).then((r) => r.data),

  reject: (id: string, notes?: string) =>
    api.put<ParkingRequest>(`/requests/${id}/reject`, { notes }).then((r) => r.data),

  cancel: (id: string) =>
    api.put<ParkingRequest>(`/requests/${id}/cancel`).then((r) => r.data),

  all: (params = {}) =>
    api.get<Page<ParkingRequest>>('/requests', { params }).then((r) => r.data),
}
