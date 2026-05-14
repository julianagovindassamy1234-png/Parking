package com.parking.service;

import com.parking.dto.response.DashboardStatsResponse;
import com.parking.enums.RequestStatus;
import com.parking.enums.SpotStatus;
import com.parking.repository.AssignmentRepository;
import com.parking.repository.ParkingRequestRepository;
import com.parking.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ParkingSpotRepository spotRepository;
    private final ParkingRequestRepository requestRepository;
    private final AssignmentRepository assignmentRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        Map<SpotStatus, Long> statusCounts = spotRepository.countByStatus();

        long available    = statusCounts.getOrDefault(SpotStatus.AVAILABLE, 0L);
        long occupied     = statusCounts.getOrDefault(SpotStatus.OCCUPIED, 0L);
        long reserved     = statusCounts.getOrDefault(SpotStatus.RESERVED, 0L);
        long maintenance  = statusCounts.getOrDefault(SpotStatus.MAINTENANCE, 0L);
        long total        = available + occupied + reserved + maintenance;

        long pending      = requestRepository.findByStatus(RequestStatus.PENDING, org.springframework.data.domain.Pageable.unpaged()).getTotalElements();
        long activeAssign = assignmentRepository.findByIsActiveTrue(org.springframework.data.domain.Pageable.unpaged()).getTotalElements();

        return new DashboardStatsResponse(total, available, occupied, reserved, maintenance, pending, activeAssign);
    }
}
