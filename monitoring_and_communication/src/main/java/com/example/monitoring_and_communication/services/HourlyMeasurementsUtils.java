package com.example.monitoring_and_communication.services;

import com.example.monitoring_and_communication.entities.Device;
import com.example.monitoring_and_communication.entities.EnergyConsumption;
import com.example.monitoring_and_communication.entities.Measurement;
import com.example.monitoring_and_communication.repositories.DeviceRepository;
import com.example.monitoring_and_communication.repositories.EnergyConsumptionRepository;
import com.example.monitoring_and_communication.websocket.TextMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HourlyMeasurementsUtils {
    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final DeviceRepository deviceRepository;
    private  final SimpMessagingTemplate template;

    @Autowired
    public HourlyMeasurementsUtils(EnergyConsumptionRepository energyConsumptionRepository, DeviceRepository deviceRepository, SimpMessagingTemplate messagingTemplate) {
        this.energyConsumptionRepository = energyConsumptionRepository;
        this.deviceRepository = deviceRepository;
        this.template = messagingTemplate;
    }

    public List<EnergyConsumption> calculateAndSaveHourlyEnergyConsumption(List<Measurement> measurements, LocalDateTime dateTime) {
        Map<UUID, Double> totalConsumptionPerDevice = measurements.stream()
                .collect(Collectors.groupingBy(
                        measurement -> measurement.getDevice().getId(),
                        Collectors.summingDouble(Measurement::getMeasurementValue)
                ));
        List<EnergyConsumption> energyConsumptions = new ArrayList<>();
        for (Map.Entry<UUID, Double> entry : totalConsumptionPerDevice.entrySet()) {
            UUID deviceId = entry.getKey();
            Double totalConsumption = entry.getValue();

            Optional<Device> device = deviceRepository.findById(deviceId);
            if (device.isPresent()) {
                EnergyConsumption energyConsumption = new EnergyConsumption();
                energyConsumption.setDateTime(dateTime.withSecond(0));
                energyConsumption.setConsumption(totalConsumption);
                energyConsumption.setDevice(device.get());
                energyConsumptions.add(energyConsumption);
                if (totalConsumption > device.get().getMaximumEnergyConsumption()) {
                    System.out.println("------>" + totalConsumption + " max e: " + device.get().getMaximumEnergyConsumption());
                    TextMessageDTO textMessageDTO = new TextMessageDTO("The device with id: " + device.get().getId() + " consumed " + totalConsumption + " kWh and has exceeded maximum consumption limit!");
                    String destination = "/topic/message/" + device.get().getUserId();
                    template.convertAndSend(destination, textMessageDTO);
                }
            } else throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceId);

        }

        return energyConsumptionRepository.saveAll(energyConsumptions);
    }


}
