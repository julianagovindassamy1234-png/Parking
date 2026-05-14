package com.parking.controller;

import com.parking.dto.request.ApprovalRequest;
import com.parking.dto.request.ParkingRequestDto;
import com.parking.dto.response.RequestResponse;
import com.parking.security.UserPrincipal;
import com.parking.service.ParkingRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ParkingRequestController {

    private final ParkingRequestService requestService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<RequestResponse> submit(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ParkingRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.submitRequest(principal, dto));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Page<RequestResponse>> myRequests(
            @AuthenticationPrincipal UserPrincipal principal, Pageable pageable) {
        return ResponseEntity.ok(requestService.getMyRequests(principal.getId(), pageable));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('MANAGEMENT', 'ADMIN')")
    public ResponseEntity<Page<RequestResponse>> pendingRequests(Pageable pageable) {
        return ResponseEntity.ok(requestService.getPendingRequests(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> getById(
            @PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(requestService.getById(id, principal));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGEMENT', 'ADMIN')")
    public ResponseEntity<RequestResponse> approve(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) ApprovalRequest body) {
        return ResponseEntity.ok(requestService.approve(id, principal, body));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGEMENT', 'ADMIN')")
    public ResponseEntity<RequestResponse> reject(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) ApprovalRequest body) {
        return ResponseEntity.ok(requestService.reject(id, principal, body));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<RequestResponse> cancel(
            @PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(requestService.cancel(id, principal));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RequestResponse>> allRequests(Pageable pageable) {
        return ResponseEntity.ok(requestService.getAllRequests(pageable));
    }
}
