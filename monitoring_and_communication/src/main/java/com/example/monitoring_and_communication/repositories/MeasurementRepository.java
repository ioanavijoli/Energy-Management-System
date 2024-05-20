package com.example.monitoring_and_communication.repositories;

import com.example.monitoring_and_communication.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {
    List<Measurement> findAllByTimestampBetween(LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}
