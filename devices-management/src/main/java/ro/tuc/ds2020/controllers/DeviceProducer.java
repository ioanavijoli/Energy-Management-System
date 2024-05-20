package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ro.tuc.ds2020.config.MessageConfig;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.DeviceRabbitDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.services.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/rabbit")
public class DeviceProducer {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add/{id}")
    public String sendDevice(@PathVariable("id") UUID deviceId) {
            DeviceDetailsDTO deviceDetailsDTO = deviceService.findDeviceById(deviceId);
            if (deviceDetailsDTO != null) {
                DeviceRabbitDTO deviceRabbitDTO = DeviceBuilder.toDeviceRabbitDTO(deviceDetailsDTO);
                template.convertAndSend(MessageConfig.EXCHANGE_NAME, MessageConfig.KEY, deviceRabbitDTO);
                return "Device details sent";
            } else {
                return "Failed to send device details: Device not found with ID - " + deviceId;
            }

    }

}
