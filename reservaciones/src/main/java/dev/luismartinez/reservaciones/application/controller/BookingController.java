package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.booking.BookinkGenericService;
import dev.luismartinez.reservaciones.domain.dto.AvailabiltyDto;
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
    public ResponseEntity<?> findTableById(@PathVariable("id") Long id) throws ReservationsException {
        BookingDto r = (BookingDto)service.findById(id);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAllTables() throws ReservationsException {
        List<BookingDto> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable("id") Long id) throws ReservationsException {
        service.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable("id") Long id, @RequestBody BookingDto dto) throws ReservationsException {
        service.update(dto, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/find-availability/{date}/{tableId}")
    public ResponseEntity<?> findCalendar(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable("tableId") Long tableId) throws ReservationsException{
        //System.out.println(date);
        List<AvailabiltyDto> list = service.findAvailability(tableId,date);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
