package com.example.monitoring_and_communication.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    @Id
    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("maximum_energy_consumption")
    private double maximumEnergyConsumption;
}
