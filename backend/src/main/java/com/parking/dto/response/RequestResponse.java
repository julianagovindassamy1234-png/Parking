package com.parking.dto.response;

import com.parking.entity.ParkingRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestResponse(
        UUID id,
        UUID requesterId,
        String requesterName,
        UUID spotId,
        String spotCode,
        String spotLocation,
        String status,
        LocalDateTime requestedAt,
        LocalDateTime respondedAt,
        LocalDateTime autoApproveDeadline,
        String notes
) {
    public static RequestResponse from(ParkingRequest r) {
        return new RequestResponse(
                r.getId(),
                r.getRequester().getId(),
                r.getRequester().getFirstName() + " " + r.getRequester().getLastName(),
                r.getSpot().getId(),
                r.getSpot().getSpotCode(),
                r.getSpot().getLocation(),
                r.getStatus().name(),
                r.getRequestedAt(),
                r.getRespondedAt(),
                r.getAutoApproveDeadline(),
                r.getNotes()
        );
    }
}
