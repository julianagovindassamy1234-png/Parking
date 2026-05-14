export type SpotStatus = 'AVAILABLE' | 'OCCUPIED' | 'RESERVED' | 'MAINTENANCE'
export type SpotType = 'STANDARD' | 'DISABLED' | 'ELECTRIC' | 'MOTORCYCLE'

export interface ParkingSpot {
  id: string
  spotCode: string
  location: string
  floor: number | null
  status: SpotStatus
  spotType: SpotType
  isActive: boolean
}

export interface ParkingAssignment {
  id: string
  userId: string
  spotId: string
  spotCode: string
  spotLocation: string
  assignedAt: string
  releasedAt: string | null
  isActive: boolean
}

export interface DashboardStats {
  totalSpots: number
  availableSpots: number
  occupiedSpots: number
  reservedSpots: number
  maintenanceSpots: number
  pendingRequests: number
  activeAssignments: number
}
