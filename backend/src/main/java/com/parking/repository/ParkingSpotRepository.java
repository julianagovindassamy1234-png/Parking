package com.parking.repository;

import com.parking.entity.ParkingSpot;
import com.parking.enums.SpotStatus;
import com.parking.enums.SpotType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, UUID> {

    @Query("""
        SELECT s FROM ParkingSpot s
        WHERE s.isActive = true
          AND (:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:status IS NULL OR s.status = :status)
          AND (:spotType IS NULL OR s.spotType = :spotType)
          AND (:floor IS NULL OR s.floor = :floor)
        """)
    Page<ParkingSpot> findWithFilters(
            @Param("location") String location,
            @Param("status") SpotStatus status,
            @Param("spotType") SpotType spotType,
            @Param("floor") Integer floor,
            Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ParkingSpot s WHERE s.id = :id")
    Optional<ParkingSpot> findByIdForUpdate(@Param("id") UUID id);

    @Query("""
        SELECT s.status AS status, COUNT(s) AS count
        FROM ParkingSpot s WHERE s.isActive = true GROUP BY s.status
        """)
    Map<SpotStatus, Long> countByStatus();
}
