package com.parking.dto.response;

import com.parking.entity.ParkingSpot;

import java.util.UUID;

public record SpotResponse(
        UUID id,
        String spotCode,
        String location,
        Integer floor,
        String status,
        String spotType,
        boolean isActive
) {
    public static SpotResponse from(ParkingSpot spot) {
        return new SpotResponse(
                spot.getId(),
                spot.getSpotCode(),
                spot.getLocation(),
                spot.getFloor(),
                spot.getStatus().name(),
                spot.getSpotType().name(),
                spot.isActive()
        );
    }
}
