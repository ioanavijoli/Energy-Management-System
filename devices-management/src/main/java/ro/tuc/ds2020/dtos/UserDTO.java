package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Device;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    @NotBlank
    private String email;
    @NotBlank
    private String username;


    public UserDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }

}
