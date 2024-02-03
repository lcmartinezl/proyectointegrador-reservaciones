package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AvailabiltyDto(
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime initDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime finishDate,
        boolean reserved,
        String reservedBy
) {
}
