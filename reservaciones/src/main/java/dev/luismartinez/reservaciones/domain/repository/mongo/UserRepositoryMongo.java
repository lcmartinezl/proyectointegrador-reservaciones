package dev.luismartinez.reservaciones.domain.repository.mongo;

import dev.luismartinez.reservaciones.domain.entity.mongo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepositoryMongo extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
}
