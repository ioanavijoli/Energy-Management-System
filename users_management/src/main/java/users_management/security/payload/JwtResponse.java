package users_management.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String token;
    private UUID id;
    private String username;
    private String email;
    private String role;
}
