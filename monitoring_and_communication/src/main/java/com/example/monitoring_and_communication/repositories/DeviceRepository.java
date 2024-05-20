package com.example.monitoring_and_communication.repositories;

import com.example.monitoring_and_communication.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
