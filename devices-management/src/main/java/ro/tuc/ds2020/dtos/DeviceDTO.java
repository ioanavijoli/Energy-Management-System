package ro.tuc.ds2020.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.User;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO  {
    private UUID id;
    private String name;
    private String address;
}
