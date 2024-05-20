package com.example.monitoring_and_communication.services;

import com.example.monitoring_and_communication.dtos.MeasurementDTO;
import com.example.monitoring_and_communication.dtos.builders.MeasurementBuilder;
import com.example.monitoring_and_communication.entities.Device;
import com.example.monitoring_and_communication.entities.EnergyConsumption;
import com.example.monitoring_and_communication.entities.Measurement;
import com.example.monitoring_and_communication.repositories.DeviceRepository;
import com.example.monitoring_and_communication.repositories.EnergyConsumptionRepository;
import com.example.monitoring_and_communication.repositories.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);

    private final MeasurementRepository measurementRepository;

    private final EnergyConsumptionRepository energyConsumptionRepository;

    private final DeviceRepository deviceRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, EnergyConsumptionRepository energyConsumptionRepository, DeviceRepository deviceRepository) {
        this.measurementRepository = measurementRepository;
        this.energyConsumptionRepository = energyConsumptionRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<MeasurementDTO> findMeasurements() {
        List<Measurement> deviceList = measurementRepository.findAll();
        return deviceList.stream()
                .map(MeasurementBuilder::toMeasurementDTO)
                .collect(Collectors.toList());
    }

    public MeasurementDTO findMeasurementById(UUID id) {
        Optional<Measurement> prosumerOptional = measurementRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Measurement with id {} was not found in db", id);
            throw new ResourceNotFoundException(Measurement.class.getSimpleName() + " with id: " + id);
        }
        return MeasurementBuilder.toMeasurementDTO(prosumerOptional.get());
    }

    public UUID insert(MeasurementDTO measurementDTO) {
        Measurement measurement = MeasurementBuilder.toEntity(measurementDTO);

        Optional<Device> deviceOptional = deviceRepository.findById(measurement.getDevice().getId());
        if (deviceOptional.isPresent()) {
            measurement.setDevice(deviceOptional.get());
            measurement = measurementRepository.save(measurement);
            LOGGER.debug("Measurement for device with id {} was inserted in db", measurement.getDevice().getId());
            return measurement.getDevice().getId();
        } else {
            LOGGER.error("Device with id {} was not found in the database", measurement.getDevice().getId());
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + measurement.getDevice().getId());
        }
    }

    public MeasurementDTO update(UUID id, MeasurementDTO measurementDTO) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (!measurementOptional.isPresent()) {
            LOGGER.error("Measurement with id {} was not found in the database", id);
            throw new ResourceNotFoundException(Measurement.class.getSimpleName() + " with id: " + id);
        }

        Measurement existingMeasurement = measurementOptional.get();
        Measurement updatedMeasurement = MeasurementBuilder.toEntity(measurementDTO);

        updatedMeasurement.setDevice(existingMeasurement.getDevice());

        Measurement savedMeasurement = measurementRepository.save(updatedMeasurement);

        LOGGER.debug("Measurement with id {} was updated in the database", id);
        return MeasurementBuilder.toMeasurementDTO(savedMeasurement);
    }

    public void delete(UUID id) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (!measurementOptional.isPresent()) {
            LOGGER.error("Measurement with id {} was not found in the database", id);
            throw new ResourceNotFoundException(Measurement.class.getSimpleName() + " with id: " + id);
        }

        measurementRepository.deleteById(id);
        LOGGER.debug("Measurement with id {} was deleted from the database", id);
    }

    public List<Measurement> getMeasurementsMadeLastHour(LocalDateTime dateTime) {
        LocalDateTime startOfHour = dateTime.withSecond(0);
        LocalDateTime endOfHour = startOfHour.withSecond(59);

        return measurementRepository.findAllByTimestampBetween(startOfHour, endOfHour);
    }

}
