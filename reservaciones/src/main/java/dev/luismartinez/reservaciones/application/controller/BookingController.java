package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.booking.BookingServiceJpa;
import dev.luismartinez.reservaciones.application.service.booking.BookingServiceMongo;
import dev.luismartinez.reservaciones.application.service.booking.BookinkGenericService;
import dev.luismartinez.reservaciones.domain.dto.Availability;
import dev.luismartinez.reservaciones.domain.dto.BookingDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public record BookingController(
        BookinkGenericService service
) {

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto dto) throws ReservationsException {
        BookingDto r  = (BookingDto)service.save(dto);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Object id) throws ReservationsException {
        BookingDto r = null;
        if (service instanceof BookingServiceJpa) {
            r = (BookingDto)service.findById(Long.parseLong((String) id));
        }
        if (service instanceof BookingServiceMongo) {
            r = (BookingDto)service.findById((String) id);
        }
        if (r != null) {
            return new ResponseEntity<>(r, HttpStatus.FOUND);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> findAll() throws ReservationsException {
        List<BookingDto> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Object id) throws ReservationsException {
        if (service instanceof BookingServiceJpa) {
            service.deleteById(Long.parseLong((String) id));
        }
        if (service instanceof BookingServiceMongo) {
            service.deleteById((String) id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Object id, @RequestBody BookingDto dto) throws ReservationsException {
        if (service instanceof BookingServiceJpa) {
            service.update(dto, Long.parseLong((String) id));
        }
        if (service instanceof BookingServiceMongo) {
            service.update(dto, (String) id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/find-availability/{date}/{tableId}")
    public ResponseEntity<?> findAvailability(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("tableId") Object tableId) throws ReservationsException{
        //System.out.println(date);
        List<Availability> list = null;

        if (service instanceof BookingServiceJpa) {
            list = service.findAvailability(Long.parseLong((String) tableId), date);
        }
        if (service instanceof BookingServiceMongo) {
            list = service.findAvailability((String) tableId,date);
        }
        if (list != null) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
