package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDto(
        Long id,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime initDate,
        Long minutesDuration,
        Long tableId,
        String customerName,
        String customerEmail,
        String customerPhoneNumber,
        int peopleNumber

) {
}
