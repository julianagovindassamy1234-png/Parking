package com.parking.service;

import com.parking.entity.ParkingRequest;
import com.parking.repository.ParkingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoApprovalScheduler {

    private final ParkingRequestRepository requestRepository;
    private final ParkingRequestService requestService;

    @Scheduled(cron = "${app.auto-approval.scheduler-cron}")
    public void processExpiredRequests() {
        List<ParkingRequest> expired = requestRepository.findExpiredPendingRequests(LocalDateTime.now());
        if (expired.isEmpty()) return;

        log.info("Auto-approving {} expired parking requests", expired.size());
        requestService.autoApprove(expired);
    }
}
