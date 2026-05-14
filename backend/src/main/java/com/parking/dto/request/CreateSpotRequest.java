package com.parking.dto.request;

import com.parking.enums.SpotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSpotRequest(
        @NotBlank String spotCode,
        @NotBlank String location,
        Integer floor,
        @NotNull SpotType spotType
) {}
