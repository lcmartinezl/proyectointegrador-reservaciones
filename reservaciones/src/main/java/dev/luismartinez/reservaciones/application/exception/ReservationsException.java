package dev.luismartinez.reservaciones.application.exception;

import dev.luismartinez.reservaciones.application.lasting.EMessage;
import org.springframework.http.HttpStatus;

public class ReservationsException extends Exception{

    private final HttpStatus status;
    private final String message;



    public ReservationsException(EMessage eMessage) {
        this.status = eMessage.getStatus();
        this.message = eMessage.getMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
