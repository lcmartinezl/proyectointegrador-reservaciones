package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.table.RestaurantTableGenericService;
import dev.luismartinez.reservaciones.domain.dto.RestaurantTableDto;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/table")
public record RestaurantTableController(
    RestaurantTableGenericService service
) {

    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody RestaurantTableDto dto) throws ReservationsException {
        RestaurantTableDto r  = (RestaurantTableDto)service.save(dto);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findTableById(@PathVariable("id") Long id) throws ReservationsException {
        RestaurantTableDto r = (RestaurantTableDto )service.findById(id);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAllTables() throws ReservationsException {
        List<RestaurantTableDto> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable("id") Long id) throws ReservationsException {
        service.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable("id") Long id, @RequestBody RestaurantTableDto dto) throws ReservationsException {
        service.update(dto, id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
