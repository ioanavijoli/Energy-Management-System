package com.example.monitoring_and_communication.repositories;

import com.example.monitoring_and_communication.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, UUID> {
    @Query("SELECT ec FROM EnergyConsumption ec " +
            "WHERE ec.device.id = :deviceId " +
            "AND ec.dateTime BETWEEN :startDateTime AND :endDateTime")
    List<EnergyConsumption> findByDeviceIdAndDateTimeRange(
            @Param("deviceId") UUID deviceId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);


}
