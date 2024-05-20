package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.services.UserService;
import ro.tuc.ds2020.services.utils.JwtTokenFilter;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public UserController(UserService userService, JwtTokenFilter jwtTokenFilter) {
        this.userService = userService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserDTO userDTO) {
        UUID userID = userService.insert(userDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);

    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID userId) {
        UserDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping(value = "/username/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username) {
        UserDTO dto = userService.findUserByUsername(username);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/devices")
    public ResponseEntity<List<DeviceDetailsDTO>> getUserDevices(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        String username = jwtTokenFilter.getUsernameFromToken(token);
        List<DeviceDetailsDTO> devices = userService.findDevicesByUsername(username);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable("id") UUID userId,
            @Valid @RequestBody UserDTO userDTO
    ) {
        UserDTO updatedUser = userService.update(userId, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        String responseMessage = "User with ID " + userId + " has been successfully deleted.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
}