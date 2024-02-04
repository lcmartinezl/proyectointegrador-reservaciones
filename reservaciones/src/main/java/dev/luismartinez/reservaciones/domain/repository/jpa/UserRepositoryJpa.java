package dev.luismartinez.reservaciones.domain.repository.jpa;

import dev.luismartinez.reservaciones.domain.entity.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryJpa extends JpaRepository<User, Long>  {
    Optional<User> findUserByEmail(String email);
}
