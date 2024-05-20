package com.example.monitoring_and_communication.dtos.builders;

import com.example.monitoring_and_communication.dtos.MeasurementDTO;
import com.example.monitoring_and_communication.entities.Device;
import com.example.monitoring_and_communication.entities.Measurement;
import org.springframework.stereotype.Component;

@Component
public class MeasurementBuilder {

    private MeasurementBuilder() {
    }


    public static MeasurementDTO toMeasurementDTO(Measurement measurement) {
        return new MeasurementDTO(measurement.getTimestamp(), measurement.getDevice().getId(), measurement.getMeasurementValue());
    }


    public static Measurement toEntity(MeasurementDTO measurementDTO) {
        Device device = new Device(measurementDTO.getDeviceId(), null, 0.0);

        // Create the Measurement entity and associate it with the resolved Device entity
        Measurement measurement = new Measurement(
                measurementDTO.getTimestamp(),
                device,
                measurementDTO.getMeasurementValue()
        );
        return measurement;
    }
}
