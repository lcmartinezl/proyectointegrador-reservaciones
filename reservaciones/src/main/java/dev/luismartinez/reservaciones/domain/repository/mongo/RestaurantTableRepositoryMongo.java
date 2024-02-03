package dev.luismartinez.reservaciones.domain.repository.mongo;

import dev.luismartinez.reservaciones.domain.entity.mongo.RestaurantTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantTableRepositoryMongo extends MongoRepository<RestaurantTable, String> {
}
