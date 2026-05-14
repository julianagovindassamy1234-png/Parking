package com.parking.controller;

import com.parking.dto.response.AssignmentResponse;
import com.parking.security.UserPrincipal;
import com.parking.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AssignmentResponse> myAssignment(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(assignmentService.getMyAssignment(principal.getId()));
    }

    @PutMapping("/{id}/release")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AssignmentResponse> release(
            @PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(assignmentService.release(id, principal));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGEMENT', 'ADMIN')")
    public ResponseEntity<Page<AssignmentResponse>> allActive(Pageable pageable) {
        return ResponseEntity.ok(assignmentService.getAllActive(pageable));
    }
}
