package com.parking.dto.response;

import com.parking.entity.ParkingAssignment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AssignmentResponse(
        UUID id,
        UUID userId,
        UUID spotId,
        String spotCode,
        String spotLocation,
        LocalDateTime assignedAt,
        LocalDateTime releasedAt,
        boolean isActive
) {
    public static AssignmentResponse from(ParkingAssignment a) {
        return new AssignmentResponse(
                a.getId(),
                a.getUser().getId(),
                a.getSpot().getId(),
                a.getSpot().getSpotCode(),
                a.getSpot().getLocation(),
                a.getAssignedAt(),
                a.getReleasedAt(),
                a.isActive()
        );
    }
}
