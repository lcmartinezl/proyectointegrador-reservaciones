package dev.luismartinez.reservaciones.domain.repository.jpa;

import dev.luismartinez.reservaciones.domain.entity.jpa.Booking;
import dev.luismartinez.reservaciones.domain.entity.jpa.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {

    List<Booking> findByTableAndInitDateBetweenOrderByInitDateAsc(RestaurantTable table, LocalDateTime from, LocalDateTime to);
    List<Booking> findAllByOrderByInitDateAsc();

    List<Booking> findByTableAndInitDateGreaterThanEqualAndInitDateLessThanOrderByInitDateAsc(RestaurantTable table, LocalDateTime dateTime1, LocalDateTime dateTime2);
}
