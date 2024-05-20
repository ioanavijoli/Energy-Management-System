package com.example.monitoring_and_communication.rabbitMq;


import com.example.monitoring_and_communication.dtos.MeasurementDTO;
import com.example.monitoring_and_communication.entities.EnergyConsumption;
import com.example.monitoring_and_communication.entities.Measurement;
import com.example.monitoring_and_communication.services.HourlyMeasurementsUtils;
import com.example.monitoring_and_communication.services.MeasurementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Component
public class MeasurementConsumer {

    private final MeasurementService measurementService;
    private final ObjectMapper objectMapper;
    private final HourlyMeasurementsUtils hourlyMeasurementsUtils;

    @Autowired
    public MeasurementConsumer(MeasurementService measurementService, ObjectMapper objectMapper, HourlyMeasurementsUtils hourlyMeasurementsUtils) {
        this.measurementService = measurementService;
        this.objectMapper = objectMapper;
        this.hourlyMeasurementsUtils = hourlyMeasurementsUtils;
    }
    ZoneId bucharestZone = ZoneId.of("Europe/Bucharest");

    ZonedDateTime zonedDateTime = ZonedDateTime.now(bucharestZone);

    LocalDateTime lastTimestamp = zonedDateTime.toLocalDateTime();
    private final DateTimeFormatter databaseTimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");


    @RabbitListener(queues = "${spring.rabbitmq.queue.devices}")
    public void receiveMessage(String jsonMessage) throws JsonProcessingException {

        System.out.println("Received message from queue: " + jsonMessage);
        MeasurementDTO measurementDTO = objectMapper.readValue(jsonMessage, MeasurementDTO.class);
        measurementService.insert(measurementDTO);

        LocalDateTime currentTimestamp = ZonedDateTime.now(bucharestZone).toLocalDateTime().withSecond(0);
        System.out.println(currentTimestamp.getMinute());
        System.out.println(lastTimestamp.getMinute());

        if (currentTimestamp.getMinute() != lastTimestamp.getMinute()) {
            List<Measurement> measurements = measurementService.getMeasurementsMadeLastHour(lastTimestamp);
            List<EnergyConsumption> energyConsumptionList = hourlyMeasurementsUtils.calculateAndSaveHourlyEnergyConsumption(measurements, lastTimestamp);
            lastTimestamp = currentTimestamp;
        }
    }
}
