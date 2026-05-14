package com.parking.dto.response;

public record DashboardStatsResponse(
        long totalSpots,
        long availableSpots,
        long occupiedSpots,
        long reservedSpots,
        long maintenanceSpots,
        long pendingRequests,
        long activeAssignments
) {}
