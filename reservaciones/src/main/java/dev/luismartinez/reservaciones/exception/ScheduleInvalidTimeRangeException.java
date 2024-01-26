package dev.luismartinez.reservaciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "schedule with invalid time range")
public class ScheduleInvalidTimeRangeException extends Exception {

    public ScheduleInvalidTimeRangeException(String message) {
        super(message);
    }
}
