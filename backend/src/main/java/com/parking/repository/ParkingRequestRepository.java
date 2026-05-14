package com.parking.repository;

import com.parking.entity.ParkingRequest;
import com.parking.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ParkingRequestRepository extends JpaRepository<ParkingRequest, UUID> {

    Page<ParkingRequest> findByRequesterId(UUID requesterId, Pageable pageable);

    Page<ParkingRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("""
        SELECT r FROM ParkingRequest r
        WHERE r.status = 'PENDING'
          AND r.autoApproveDeadline < :now
        ORDER BY r.requestedAt ASC
        """)
    List<ParkingRequest> findExpiredPendingRequests(@Param("now") LocalDateTime now);

    @Query("""
        SELECT COUNT(r) > 0 FROM ParkingRequest r
        WHERE r.spot.id = :spotId
          AND r.status IN ('PENDING', 'APPROVED', 'AUTO_APPROVED')
        """)
    boolean hasActivePendingOrApprovedRequest(@Param("spotId") UUID spotId);
}
