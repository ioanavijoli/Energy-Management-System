package com.example.monitoring_and_communication.dtos;

import com.example.monitoring_and_communication.entities.Device;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionDTO {

    private UUID id;
    private LocalDateTime dateTime;
    private double consumption;
    @JsonIgnore
    private Device device;
}
