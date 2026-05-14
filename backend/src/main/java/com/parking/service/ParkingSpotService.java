package com.parking.service;

import com.parking.dto.request.CreateSpotRequest;
import com.parking.dto.request.UpdateSpotRequest;
import com.parking.dto.response.SpotResponse;
import com.parking.entity.ParkingSpot;
import com.parking.enums.SpotStatus;
import com.parking.enums.SpotType;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {

    private final ParkingSpotRepository spotRepository;

    @Transactional(readOnly = true)
    public Page<SpotResponse> findAll(String location, SpotStatus status, SpotType spotType, Integer floor, Pageable pageable) {
        return spotRepository.findWithFilters(location, status, spotType, floor, pageable)
                .map(SpotResponse::from);
    }

    @Transactional(readOnly = true)
    public SpotResponse findById(UUID id) {
        return spotRepository.findById(id)
                .map(SpotResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingSpot", id));
    }

    @Transactional
    public SpotResponse create(CreateSpotRequest request) {
        ParkingSpot spot = ParkingSpot.builder()
                .spotCode(request.spotCode())
                .location(request.location())
                .floor(request.floor())
                .spotType(request.spotType())
                .build();
        return SpotResponse.from(spotRepository.save(spot));
    }

    @Transactional
    public SpotResponse update(UUID id, UpdateSpotRequest request) {
        ParkingSpot spot = spotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingSpot", id));

        if (request.spotCode() != null) spot.setSpotCode(request.spotCode());
        if (request.location() != null) spot.setLocation(request.location());
        if (request.floor() != null) spot.setFloor(request.floor());
        if (request.spotType() != null) spot.setSpotType(request.spotType());
        if (request.status() != null) spot.setStatus(request.status());

        return SpotResponse.from(spotRepository.save(spot));
    }

    @Transactional
    public void delete(UUID id) {
        ParkingSpot spot = spotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingSpot", id));
        spot.setActive(false);
        spotRepository.save(spot);
    }
}
