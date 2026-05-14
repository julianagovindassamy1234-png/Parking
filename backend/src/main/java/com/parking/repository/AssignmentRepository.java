package com.parking.repository;

import com.parking.entity.ParkingAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<ParkingAssignment, UUID> {

    Optional<ParkingAssignment> findByUserIdAndIsActiveTrue(UUID userId);

    Page<ParkingAssignment> findByIsActiveTrue(Pageable pageable);
}
