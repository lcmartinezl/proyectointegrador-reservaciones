package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDto(
        Object id,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime initDate,
        Long minutesDuration,
        Object tableId,
        String customerName,
        String customerEmail,
        String customerPhoneNumber,
        int peopleNumber

) {
}
