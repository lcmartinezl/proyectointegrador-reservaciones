package dev.luismartinez.reservaciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "another schedule is on same day and time")
public class SchedulesOnSameDayAndTime extends RuntimeException {
}
// conflicto