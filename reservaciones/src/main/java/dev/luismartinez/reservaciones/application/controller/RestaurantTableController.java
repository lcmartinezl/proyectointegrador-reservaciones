package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
import dev.luismartinez.reservaciones.application.service.table.RestaurantTableGenericService;
import dev.luismartinez.reservaciones.application.service.table.RestaurantTableServiceJpa;
import dev.luismartinez.reservaciones.application.service.table.RestaurantTableServiceMongo;
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
    public ResponseEntity<?> findTableById(@PathVariable("id") Object id) throws ReservationsException {
        RestaurantTableDto r = null;
        if (service instanceof RestaurantTableServiceJpa) {
            r = (RestaurantTableDto )service.findById(Long.parseLong((String)id));
        }
        if (service instanceof RestaurantTableServiceMongo) {
            r = (RestaurantTableDto )service.findById((String)id);
        }
        if (r != null) {
            return new ResponseEntity<>(r, HttpStatus.FOUND);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping
    public ResponseEntity<?> findAllTables() throws ReservationsException {
        List<RestaurantTableDto> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable("id") Object id) throws ReservationsException {
        if (service instanceof RestaurantTableServiceJpa) {
            service.deleteById(Long.parseLong((String) id));
        }
        if (service instanceof RestaurantTableServiceMongo) {
            service.deleteById((String) id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable("id") Object id, @RequestBody RestaurantTableDto dto) throws ReservationsException {
        if (service instanceof RestaurantTableServiceJpa) {
            service.update(dto, Long.parseLong((String) id));
        }
        if (service instanceof RestaurantTableServiceMongo) {
            service.update(dto, (String) id);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
