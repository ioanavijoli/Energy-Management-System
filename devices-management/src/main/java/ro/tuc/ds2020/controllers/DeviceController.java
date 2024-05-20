package ro.tuc.ds2020.controllers;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.config.MessageConfig;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.DeviceRabbitDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.utils.JwtTokenFilter;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final RabbitTemplate template;

    @Autowired
    public DeviceController(DeviceService deviceService, RabbitTemplate rabbitTemplate) {
        this.deviceService = deviceService;
        this.template = rabbitTemplate;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DeviceDetailsDTO>> getDevices() {
        List<DeviceDetailsDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);

    }


    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        if (deviceDTO != null) {
            deviceDTO.setId(deviceID);
            DeviceRabbitDTO deviceRabbitDTO = DeviceBuilder.toDeviceRabbitDTO(deviceDTO);
            template.convertAndSend(MessageConfig.EXCHANGE_NAME, MessageConfig.KEY, deviceRabbitDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(
            @PathVariable("id") UUID deviceId,
            @Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO
    ) {
        DeviceDetailsDTO updatedDevice = deviceService.update(deviceId, deviceDetailsDTO);
        if (updatedDevice != null) {
            updatedDevice.setId(deviceId);
            DeviceRabbitDTO deviceRabbitDTO = DeviceBuilder.toDeviceRabbitDTO(updatedDevice);
            template.convertAndSend(MessageConfig.EXCHANGE_NAME, MessageConfig.KEY, deviceRabbitDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.delete(deviceId);
        String responseMessage = "Device with ID " + deviceId + " has been successfully deleted.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

}
