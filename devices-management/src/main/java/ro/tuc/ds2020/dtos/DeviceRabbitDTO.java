package ro.tuc.ds2020.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRabbitDTO  {

    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("maximum_energy_consumption")
    private double maximumEnergyConsumption;
}
