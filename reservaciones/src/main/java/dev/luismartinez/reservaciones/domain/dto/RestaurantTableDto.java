package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantTableDto(
        Object id,
        String name,
        Integer totalSeats
) {
}
