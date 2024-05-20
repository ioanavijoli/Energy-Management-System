package users_management.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User  implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "age", nullable = false)
    private int age;

    private Role role;

    public enum Role {
        ADMIN, CLIENT
    }

    public User(String email, String username, String password,  Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.age = age;
        this.role = role;
    }
}
