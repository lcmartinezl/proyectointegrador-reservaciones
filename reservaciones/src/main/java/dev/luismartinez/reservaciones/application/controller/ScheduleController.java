package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleGenericSerive;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public record ScheduleController(
        ScheduleGenericSerive scheduleService
) {

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleDto scheduleDto) throws ReservationsException {
        ScheduleDto dto = (ScheduleDto)scheduleService.save(scheduleDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllSchedules() throws ReservationsException{
        List<ScheduleDto> list = scheduleService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws ReservationsException {
        ScheduleDto scheduleDto = (ScheduleDto)scheduleService.findById(id);
        return new ResponseEntity<>(scheduleDto, HttpStatus.FOUND);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable("id") Long id, @RequestBody ScheduleDto scheduleDto) throws ReservationsException {
        scheduleService.update(scheduleDto, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws ReservationsException {
        scheduleService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
