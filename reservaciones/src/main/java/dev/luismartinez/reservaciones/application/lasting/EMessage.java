package dev.luismartinez.reservaciones.application.lasting;

import org.springframework.http.HttpStatus;

public enum EMessage {

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "The schedule was not found"),
    SCHEDULE_SAME_DAYTIME(HttpStatus.CONFLICT, "Another schedule is on same day and time"),
    SCHEDULE_SAME_DAYOFWEEK(HttpStatus.CONFLICT, "The schedule for the day is already defined"),
    SCHEDULE_INVALID_TIME_RANGE(HttpStatus.CONFLICT, "schedule with invalid time range"),
    SCHEDULE_INVALID_MINIMUN_RANGE(HttpStatus.CONFLICT, "Time range can't be less than 30 minutes"),

    TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "The table was not found"),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "The reservation was not found"),
    RESERVATION_NO_SCHEDULE_FOUND(HttpStatus.CONFLICT, "No Schedule available for the date/time of the Reservation"),
    RESERVATION_SAME_DAYTIME(HttpStatus.CONFLICT, "The table is already reserved for the date/time requested"),
    RESERVATION_NO_SEATS_AVAILABLE(HttpStatus.CONFLICT, "The reservation has more number of people than the seats available for the table");
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
