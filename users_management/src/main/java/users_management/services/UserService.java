package users_management.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import users_management.controllers.handlers.exceptions.model.ResourceNotFoundException;
import users_management.dtos.UserDTO;
import users_management.dtos.UserDetailsDTO;
import users_management.dtos.builders.UserBuilder;
import users_management.entities.User;
import users_management.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }
    public List<UserDetailsDTO> findUsersDetails() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDetailsDTO)
                .collect(Collectors.toList());
    }


    public UserDetailsDTO findUserById(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDetailsDTO(prosumerOptional.get());
    }
    public UUID findIdByUsername(String username){
        User user = userRepository.findByUsername(username);
        if (user== null) {
            LOGGER.error("User with username {} was not found in db", username);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with username: " + username);
        }
        return user.getId();
    }
    public List <String> findUsernamesByRole(User.Role role){
        List<User> userList = userRepository.findAllByRole(role);
        if(userList.isEmpty())
        {
            LOGGER.error("No admins to chat with were found");
            throw new ResourceNotFoundException(User.class.getSimpleName());
        }
        List <String> usernames = new ArrayList<>();
        for(User user: userList)
            usernames.add(user.getUsername());
        return usernames;
    }


    public UUID insert(UserDetailsDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public UserDetailsDTO update(UUID id, UserDetailsDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in the database", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        User existingUser = userOptional.get();
        User updatedUser = UserBuilder.toEntity(userDTO);

        updatedUser.setId(existingUser.getId());
        updatedUser.setPassword(userDTO.getPassword());

        User savedUser = userRepository.save(updatedUser);

        LOGGER.debug("User with id {} was updated in the database", id);
        return UserBuilder.toUserDetailsDTO(savedUser);
    }

    public void delete(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in the database", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        userRepository.deleteById(id);
        LOGGER.debug("User with id {} was deleted from the database", id);
    }



}
