package com.example.monitoring_and_communication.services;

import com.example.monitoring_and_communication.dtos.EnergyConsumptionDTO;
import com.example.monitoring_and_communication.dtos.builders.EnergyConsumptionBuilder;
import com.example.monitoring_and_communication.entities.EnergyConsumption;
import com.example.monitoring_and_communication.repositories.EnergyConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnergyConsumptionService {
    private final EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    public EnergyConsumptionService(EnergyConsumptionRepository energyConsumptionRepository) {
        this.energyConsumptionRepository = energyConsumptionRepository;
    }
    public List<EnergyConsumptionDTO> getEnergyConsumptionByDateAndByDevice(UUID deviceId, LocalDateTime startTime, LocalDateTime endTime){
        List<EnergyConsumption> energyConsumptionList = energyConsumptionRepository.findByDeviceIdAndDateTimeRange(deviceId, startTime, endTime);

        return energyConsumptionList.stream()
                .map(EnergyConsumptionBuilder::toEnergyConsumptionDTO)
                .collect(Collectors.toList());
    }

}
