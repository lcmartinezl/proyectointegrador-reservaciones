package dev.luismartinez.reservaciones.domain.repository.jpa;

import dev.luismartinez.reservaciones.domain.entity.jpa.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepositoryJpa extends JpaRepository<RestaurantTable,Long> {
}
