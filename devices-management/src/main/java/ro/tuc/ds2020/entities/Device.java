package ro.tuc.ds2020.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Device {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    private String name;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;


    @Column(name = "address", nullable = false)
    @NotBlank
    private String address;

    @Column(name = "maximum_consumption", nullable = false)
    @NotNull
    private int maximumEnergyConsumption;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Device( String name, String description, String address, int maximumEnergyConsumption, User user) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.maximumEnergyConsumption = maximumEnergyConsumption;
        this.user = user;
    }

}
