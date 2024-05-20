package users_management.dtos.builders;

import users_management.dtos.UserDTO;
import users_management.dtos.UserDetailsDTO;
import users_management.entities.User;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername());
    }

    public static UserDetailsDTO toUserDetailsDTO(User user) {
        return new UserDetailsDTO(user.getId(), user.getEmail(), user.getUsername(), user.getPassword(), user.getAge(), user.getRole());
    }
    public static UserDTO fromUserDetailsDTOToUserDTO(UserDetailsDTO userDetailsDTO) {
        return new UserDTO(userDetailsDTO.getId(), userDetailsDTO.getEmail(), userDetailsDTO.getUsername());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO) {
        return new User(userDetailsDTO.getId(), userDetailsDTO.getEmail(), userDetailsDTO.getUsername(), userDetailsDTO.getPassword(), userDetailsDTO.getAge(), userDetailsDTO.getRole());
    }

}
