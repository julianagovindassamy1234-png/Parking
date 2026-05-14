export type RequestStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'AUTO_APPROVED' | 'CANCELLED'

export interface ParkingRequest {
  id: string
  requesterId: string
  requesterName: string
  spotId: string
  spotCode: string
  spotLocation: string
  status: RequestStatus
  requestedAt: string
  respondedAt: string | null
  autoApproveDeadline: string
  notes: string | null
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}
