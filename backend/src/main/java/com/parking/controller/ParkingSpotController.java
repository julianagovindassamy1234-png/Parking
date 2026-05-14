package com.parking.controller;

import com.parking.dto.request.CreateSpotRequest;
import com.parking.dto.request.UpdateSpotRequest;
import com.parking.dto.response.SpotResponse;
import com.parking.enums.SpotStatus;
import com.parking.enums.SpotType;
import com.parking.service.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService spotService;

    @GetMapping
    public ResponseEntity<Page<SpotResponse>> list(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) SpotStatus status,
            @RequestParam(required = false) SpotType spotType,
            @RequestParam(required = false) Integer floor,
            Pageable pageable) {
        return ResponseEntity.ok(spotService.findAll(location, status, spotType, floor, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(spotService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotResponse> create(@Valid @RequestBody CreateSpotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spotService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpotResponse> update(@PathVariable UUID id, @RequestBody UpdateSpotRequest request) {
        return ResponseEntity.ok(spotService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        spotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
