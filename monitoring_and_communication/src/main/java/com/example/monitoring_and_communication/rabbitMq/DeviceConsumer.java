package com.example.monitoring_and_communication.rabbitMq;

import com.example.monitoring_and_communication.dtos.DeviceDTO;
import com.example.monitoring_and_communication.services.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceConsumer {

    private final DeviceService deviceService;
    private ObjectMapper objectMapper;


    @Autowired
    public DeviceConsumer(DeviceService deviceService, ObjectMapper objectMapper) {
        this.deviceService = deviceService;
        this.objectMapper = objectMapper;

    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.new}")
    public void receiveDevice(String jsonMessage) throws JsonProcessingException {
        System.out.println("Received message from queue: " + jsonMessage);
        DeviceDTO deviceDTO = objectMapper.readValue(jsonMessage, DeviceDTO.class);
        deviceService.insertOrUpdateDevice(deviceDTO);

    }


}
