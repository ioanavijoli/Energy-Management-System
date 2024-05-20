package com.example.monitoring_and_communication.services;

import com.example.monitoring_and_communication.dtos.DeviceDTO;
import com.example.monitoring_and_communication.dtos.builders.DeviceBuilder;
import com.example.monitoring_and_communication.entities.Device;
import com.example.monitoring_and_communication.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void insertOrUpdateDevice(DeviceDTO deviceDTO) {
        Optional<Device> existingDevice = deviceRepository.findById(deviceDTO.getId());

        if (existingDevice.isPresent()) {
            Device foundDevice = existingDevice.get();
            foundDevice.setUserId(deviceDTO.getUserId());
            foundDevice.setMaximumEnergyConsumption(deviceDTO.getMaximumEnergyConsumption());
            deviceRepository.save(foundDevice);
        } else {
            Device device = DeviceBuilder.toEntity(deviceDTO);
            deviceRepository.save(device);
        }
    }
}
