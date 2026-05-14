import api from './axios'
import type { AuthResponse, User } from '@/types/auth'

export const authApi = {
  login: (email: string, password: string) =>
    api.post<AuthResponse>('/auth/login', { email, password }).then((r) => r.data),

  refresh: (refreshToken: string) =>
    api.post<AuthResponse>('/auth/refresh', null, {
      headers: { 'X-Refresh-Token': refreshToken },
    }).then((r) => r.data),

  logout: () => api.post('/auth/logout'),

  me: () => api.get<User>('/auth/me').then((r) => r.data),
}
