package dev.sigit.springbootjwt.repository;

import dev.sigit.springbootjwt.entities.Role;
import dev.sigit.springbootjwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}
