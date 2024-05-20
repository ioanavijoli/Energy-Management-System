package com.example.monitoring_and_communication.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "measurements")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;


    @Column(name = "measurement_value", nullable = false)
    private Double measurementValue;

    public Measurement(LocalDateTime timestamp, Device device, Double measurementValue) {
        this.timestamp = timestamp;
        this.device = device;
        this.measurementValue = measurementValue;
    }


}