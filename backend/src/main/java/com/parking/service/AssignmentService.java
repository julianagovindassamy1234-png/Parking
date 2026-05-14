package com.parking.service;

import com.parking.dto.response.AssignmentResponse;
import com.parking.entity.ParkingAssignment;
import com.parking.enums.SpotStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.AssignmentRepository;
import com.parking.repository.ParkingSpotRepository;
import com.parking.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ParkingSpotRepository spotRepository;

    @Transactional(readOnly = true)
    public AssignmentResponse getMyAssignment(UUID userId) {
        return assignmentRepository.findByUserIdAndIsActiveTrue(userId)
                .map(AssignmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No active assignment found"));
    }

    @Transactional
    public AssignmentResponse release(UUID assignmentId, UserPrincipal principal) {
        ParkingAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingAssignment", assignmentId));

        if (!assignment.getUser().getId().equals(principal.getId())) {
            throw new AccessDeniedException("Not authorized to release this assignment");
        }
        if (!assignment.isActive()) {
            throw new IllegalStateException("Assignment is already released");
        }

        assignment.setActive(false);
        assignment.setReleasedAt(LocalDateTime.now());
        assignment.getSpot().setStatus(SpotStatus.AVAILABLE);
        spotRepository.save(assignment.getSpot());

        return AssignmentResponse.from(assignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public Page<AssignmentResponse> getAllActive(Pageable pageable) {
        return assignmentRepository.findByIsActiveTrue(pageable).map(AssignmentResponse::from);
    }
}
