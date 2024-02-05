package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// Dto para mostrar informacion de la disponilibidad de mesa para un dia
public record Availability(
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime initDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime finishDate,
        boolean reserved,
        String reservedBy
) {
}
