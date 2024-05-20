package users_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import users_management.dtos.validators.annotation.AgeLimit;
import users_management.entities.User.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private UUID id;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;


    @AgeLimit(limit = 17)
    private int age;

    private Role role;

    public UserDetailsDTO(String email, String username, String password, int age, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsDTO that = (UserDetailsDTO) o;
        return age == that.age &&
                Objects.equals(username, that.username)
                && Objects.equals(email, that.email)
                && Objects.equals(password, that.password)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, password, age, role);
    }

    public void setId(UUID userID) {
        this.id = userID;
    }
}
