package com.example.monitoring_and_communication.dtos.builders;

import com.example.monitoring_and_communication.dtos.EnergyConsumptionDTO;
import com.example.monitoring_and_communication.entities.EnergyConsumption;
import org.springframework.stereotype.Component;

@Component
public class EnergyConsumptionBuilder {

    private EnergyConsumptionBuilder() {
    }


    public static EnergyConsumptionDTO toEnergyConsumptionDTO(EnergyConsumption enrgyConsumption) {
        return new EnergyConsumptionDTO(enrgyConsumption.getId(), enrgyConsumption.getDateTime(), enrgyConsumption.getConsumption(), enrgyConsumption.getDevice());
    }
}
