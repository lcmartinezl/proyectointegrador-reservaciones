package dev.luismartinez.reservaciones.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppointmentDTO (
    Long id,
    String customerName,
    String customerEmail,
    String customerPhoneNumber,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
     LocalDateTime initDate,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    LocalDateTime finishDate



) {

        }
