package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {


    @Modifying
    @Transactional
    @Query("UPDATE Device d SET d.user.id = null WHERE d.user = ?1")
    void deleteUserFromDevices(User user);

    @Modifying
    @Transactional
    @Query("SELECT d FROM Device d WHERE d.user.username = ?1")
    List<Device> findDevicesByUsername(String username);

}
