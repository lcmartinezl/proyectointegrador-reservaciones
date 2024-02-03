package dev.luismartinez.reservaciones.domain.repository.mongo;

import dev.luismartinez.reservaciones.domain.entity.mongo.RestaurantTable;
import dev.luismartinez.reservaciones.domain.entity.mongo.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryMongo extends MongoRepository<Booking, String> {

    List<Booking> findByTableAndInitDateBetweenOrderByInitDateAsc(RestaurantTable table, LocalDateTime from, LocalDateTime to);
    List<Booking> findAllByOrderByInitDateAsc();
    List<Booking> findByTableAndInitDateGreaterThanEqualAndFinishDateLessThanOrderByInitDateAsc(RestaurantTable table, LocalDateTime dateTime1, LocalDateTime dateTime2);
}
