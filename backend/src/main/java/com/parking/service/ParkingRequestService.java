package com.parking.service;

import com.parking.config.AppProperties;
import com.parking.dto.request.ApprovalRequest;
import com.parking.dto.request.ParkingRequestDto;
import com.parking.dto.response.RequestResponse;
import com.parking.entity.ParkingAssignment;
import com.parking.entity.ParkingRequest;
import com.parking.entity.ParkingSpot;
import com.parking.entity.User;
import com.parking.enums.RequestStatus;
import com.parking.enums.SpotStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.exception.SpotAlreadyTakenException;
import com.parking.repository.AssignmentRepository;
import com.parking.repository.ParkingRequestRepository;
import com.parking.repository.ParkingSpotRepository;
import com.parking.repository.UserRepository;
import com.parking.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingRequestService {

    private final ParkingRequestRepository requestRepository;
    private final ParkingSpotRepository spotRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final AppProperties appProperties;

    @Transactional
    public RequestResponse submitRequest(UserPrincipal principal, ParkingRequestDto dto) {
        ParkingSpot spot = spotRepository.findByIdForUpdate(dto.spotId())
                .orElseThrow(() -> new ResourceNotFoundException("ParkingSpot", dto.spotId()));

        if (spot.getStatus() != SpotStatus.AVAILABLE) {
            throw new SpotAlreadyTakenException(spot.getSpotCode());
        }
        if (requestRepository.hasActivePendingOrApprovedRequest(spot.getId())) {
            throw new SpotAlreadyTakenException(spot.getSpotCode());
        }

        User requester = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));

        ParkingRequest request = ParkingRequest.builder()
                .requester(requester)
                .spot(spot)
                .autoApproveDeadline(LocalDateTime.now().plusHours(appProperties.getWindowHours()))
                .build();

        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public Page<RequestResponse> getMyRequests(UUID userId, Pageable pageable) {
        return requestRepository.findByRequesterId(userId, pageable).map(RequestResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<RequestResponse> getPendingRequests(Pageable pageable) {
        return requestRepository.findByStatus(RequestStatus.PENDING, pageable).map(RequestResponse::from);
    }

    @Transactional(readOnly = true)
    public RequestResponse getById(UUID id, UserPrincipal principal) {
        ParkingRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingRequest", id));

        boolean isOwner = request.getRequester().getId().equals(principal.getId());
        boolean isPrivileged = principal.getRole().equals("MANAGEMENT") || principal.getRole().equals("ADMIN");
        if (!isOwner && !isPrivileged) {
            throw new AccessDeniedException("Not authorized to view this request");
        }
        return RequestResponse.from(request);
    }

    @Transactional
    public RequestResponse approve(UUID requestId, UserPrincipal approver, ApprovalRequest body) {
        ParkingRequest request = getAndLockRequest(requestId, RequestStatus.PENDING);
        User manager = userRepository.findById(approver.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", approver.getId()));

        request.setStatus(RequestStatus.APPROVED);
        request.setRespondedAt(LocalDateTime.now());
        request.setRespondedBy(manager);
        if (body != null) request.setNotes(body.notes());

        createAssignment(request);
        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional
    public RequestResponse reject(UUID requestId, UserPrincipal approver, ApprovalRequest body) {
        ParkingRequest request = getAndLockRequest(requestId, RequestStatus.PENDING);
        User manager = userRepository.findById(approver.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", approver.getId()));

        request.setStatus(RequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        request.setRespondedBy(manager);
        if (body != null) request.setNotes(body.notes());

        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional
    public RequestResponse cancel(UUID requestId, UserPrincipal principal) {
        ParkingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingRequest", requestId));

        if (!request.getRequester().getId().equals(principal.getId())) {
            throw new AccessDeniedException("Not authorized to cancel this request");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be cancelled");
        }
        request.setStatus(RequestStatus.CANCELLED);
        return RequestResponse.from(requestRepository.save(request));
    }

    @Transactional
    public void autoApprove(List<ParkingRequest> expiredRequests) {
        for (ParkingRequest request : expiredRequests) {
            request.setStatus(RequestStatus.AUTO_APPROVED);
            request.setRespondedAt(LocalDateTime.now());
            createAssignment(request);
            requestRepository.save(request);
        }
    }

    @Transactional(readOnly = true)
    public Page<RequestResponse> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable).map(RequestResponse::from);
    }

    private ParkingRequest getAndLockRequest(UUID id, RequestStatus expectedStatus) {
        ParkingRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingRequest", id));
        if (request.getStatus() != expectedStatus) {
            throw new IllegalStateException("Request is not in " + expectedStatus + " status");
        }
        return request;
    }

    private void createAssignment(ParkingRequest request) {
        ParkingSpot spot = request.getSpot();
        spot.setStatus(SpotStatus.OCCUPIED);
        spotRepository.save(spot);

        ParkingAssignment assignment = ParkingAssignment.builder()
                .request(request)
                .user(request.getRequester())
                .spot(spot)
                .build();
        assignmentRepository.save(assignment);
    }
}
