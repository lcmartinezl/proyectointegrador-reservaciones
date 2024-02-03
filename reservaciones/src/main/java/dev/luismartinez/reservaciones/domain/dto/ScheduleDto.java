package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.DayOfWeek;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScheduleDto(
    Long id,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    DayOfWeek dayOfWeek,
    @JsonFormat(pattern="HH:mm:ss")
    LocalTime initTime,
    @JsonFormat(pattern="HH:mm:ss")
    LocalTime finishTime
) {
}
