package dev.luismartinez.reservaciones.application.lasting;

import org.springframework.http.HttpStatus;

public enum EMessage {

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "The schedule was not found"),
    SCHEDULE_SAME_DAYTIME(HttpStatus.CONFLICT, "Another schedule is on same day and time"),
    SCHEDULE_INVALID_TIME_RANGE(HttpStatus.CONFLICT, "schedule with invalid time range"),
    SCHEDULE_INVALID_MINIMUN_RANGE(HttpStatus.CONFLICT, "Time range can't be less than 30 minutes");

    private final HttpStatus status;
    private final String message;
    EMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
