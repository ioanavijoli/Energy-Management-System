package com.example.monitoring_and_communication.dtos.builders;

import com.example.monitoring_and_communication.dtos.DeviceDTO;
import com.example.monitoring_and_communication.dtos.MeasurementDTO;
import com.example.monitoring_and_communication.entities.Device;
import com.example.monitoring_and_communication.entities.Measurement;
import org.springframework.stereotype.Component;

@Component
public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getUserId(), device.getMaximumEnergyConsumption());
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getUserId(), deviceDTO.getMaximumEnergyConsumption());
    }
}
