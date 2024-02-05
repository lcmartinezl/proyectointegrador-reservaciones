package dev.luismartinez.reservaciones.application.service.booking;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.GenericService;
import dev.luismartinez.reservaciones.domain.dto.Availability;

import java.time.LocalDate;
import java.util.List;

public interface BookinkGenericService <T, Object> extends GenericService<T, Object> {

    List<Availability> findAvailability(Object tableId, LocalDate date) throws ReservationsException;
}
