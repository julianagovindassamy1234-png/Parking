package com.parking.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParkingRequestDto(
        @NotNull UUID spotId
) {}
