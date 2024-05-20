package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDetailsDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(prosumerOptional.get());
    }

    public UUID insert(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public DeviceDetailsDTO update(UUID id, DeviceDetailsDTO deviceDTO) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in the database", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        Device existingDevice = deviceOptional.get();
        Device updatedDevice = DeviceBuilder.toEntity(deviceDTO);

        updatedDevice.setId(existingDevice.getId());

        Device savedDevice = deviceRepository.save(updatedDevice);

        LOGGER.debug("Device with id {} was updated in the database", id);
        return DeviceBuilder.toDeviceDetailsDTO(savedDevice);
    }

    public void delete(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in the database", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        LOGGER.debug("Device with id {} was deleted from the database", id);
    }



}
