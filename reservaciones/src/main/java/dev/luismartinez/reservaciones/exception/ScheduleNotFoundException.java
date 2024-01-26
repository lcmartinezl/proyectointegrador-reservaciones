package dev.luismartinez.reservaciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "schedule not found")
public class ScheduleNotFoundException extends RuntimeException{
}
