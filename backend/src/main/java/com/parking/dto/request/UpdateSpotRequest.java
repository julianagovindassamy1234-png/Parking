package com.parking.dto.request;

import com.parking.enums.SpotStatus;
import com.parking.enums.SpotType;

public record UpdateSpotRequest(
        String spotCode,
        String location,
        Integer floor,
        SpotType spotType,
        SpotStatus status
) {}
