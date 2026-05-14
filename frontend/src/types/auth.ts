export type UserRole = 'EMPLOYEE' | 'MANAGEMENT' | 'ADMIN'

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  role: UserRole
  level: number | null
  isActive: boolean
}

export interface AuthResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  user: User
}
