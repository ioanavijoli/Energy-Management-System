package com.example.monitoring_and_communication.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "devices")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("maximum_energy_consumption")
    private double maximumEnergyConsumption;

}
