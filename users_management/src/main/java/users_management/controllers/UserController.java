package users_management.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import users_management.dtos.UserDTO;
import users_management.dtos.UserDetailsDTO;
import users_management.dtos.builders.UserBuilder;
import users_management.entities.User;
import users_management.security.jwt.JwtUtils;
import users_management.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDetailsDTO>> getUsersDetails() {
        List<UserDetailsDTO> dtos = userService.findUsersDetails();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    @GetMapping(value = "/id")
    public ResponseEntity<UUID> getIdFromUsername(@RequestParam String username) {
        UUID id = userService.findIdByUsername(username);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("id") UUID userId) {
        UserDetailsDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/chat-admins")
    public ResponseEntity<List <String>> getAdminsToChatWith() {
        List<String> usernames = userService.findUsernamesByRole(User.Role.ADMIN);
        return new ResponseEntity<>(usernames, HttpStatus.OK);
    }

    @GetMapping(value = "/chat-clients")
    public ResponseEntity<List <String>> getClientsToChatWith() {
        List<String> usernames = userService.findUsernamesByRole(User.Role.CLIENT);
        return new ResponseEntity<>(usernames, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserDetailsDTO userDetailsDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UUID userID = null;
        userID = userService.insert(userDetailsDTO);
        userDetailsDTO.setId(userID);
        UserDTO userDTO = UserBuilder.fromUserDetailsDTOToUserDTO(userDetailsDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        restTemplate.exchange("http://devicesmanagement:8081/user", HttpMethod.POST, requestEntity, UserDTO.class);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<UserDetailsDTO> updateUser(
            @PathVariable("id") UUID userId,
            @Valid @RequestBody UserDetailsDTO userDetailsDTO,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        UserDetailsDTO updatedUser = userService.update(userId, userDetailsDTO);
        UserDTO userDTO = UserBuilder.fromUserDetailsDTOToUserDTO(updatedUser);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        restTemplate.exchange("http://devicesmanagement:8081/user/" + userId, HttpMethod.PUT, requestEntity, UserDTO.class);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID userId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        userService.delete(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange("http://devicesmanagement:8081/user/" + userId, HttpMethod.DELETE, requestEntity, Void.class);
        String responseMessage = "User with ID " + userId + " has been successfully deleted.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
