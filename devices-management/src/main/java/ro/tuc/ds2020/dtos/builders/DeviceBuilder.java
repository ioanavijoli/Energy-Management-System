package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.DeviceRabbitDTO;
import ro.tuc.ds2020.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getName(), device.getAddress());
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(), device.getName(), device.getDescription(), device.getAddress(), device.getMaximumEnergyConsumption(), device.getUser());
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        return new Device( deviceDetailsDTO.getId(), deviceDetailsDTO.getName(), deviceDetailsDTO.getDescription(), deviceDetailsDTO.getAddress(), deviceDetailsDTO.getMaximumEnergyConsumption(), deviceDetailsDTO.getUser());
    }
    public static DeviceRabbitDTO toDeviceRabbitDTO(DeviceDetailsDTO deviceDetailsDTO){
        return  new DeviceRabbitDTO(deviceDetailsDTO.getId(), deviceDetailsDTO.getUser().getId(), deviceDetailsDTO.getMaximumEnergyConsumption());
    }
}
