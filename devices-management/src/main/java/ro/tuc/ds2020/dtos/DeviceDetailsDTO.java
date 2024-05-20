package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tuc.ds2020.entities.Device.*;
import ro.tuc.ds2020.entities.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetailsDTO {

    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String address;
    @NotNull
    private int maximumEnergyConsumption;

    private User user;

    public DeviceDetailsDTO(String name, String description, String address, int maximumEnergyConsumption, User user) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.maximumEnergyConsumption = maximumEnergyConsumption;
        this.user = user;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return  Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(address, that.address)
                && maximumEnergyConsumption == that.maximumEnergyConsumption;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description,address,maximumEnergyConsumption);
    }
}
