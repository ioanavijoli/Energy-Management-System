package users_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import users_management.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // You can define custom query methods here if needed.
    User findByUsername(String username);
    List<User> findAllByRole(User.Role role);
}
