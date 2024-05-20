package com.example.monitoring_and_communication.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDTO {

    @NotNull
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("device_id")
    private UUID deviceId;

    @NotNull
    @JsonProperty("measurement_value")
    private Double measurementValue;

}