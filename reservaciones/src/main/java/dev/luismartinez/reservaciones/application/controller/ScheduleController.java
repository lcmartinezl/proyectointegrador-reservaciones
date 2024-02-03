package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleGenericService;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceMongo;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public record ScheduleController(
        ScheduleGenericService scheduleService
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
    public ResponseEntity<?> findById(@PathVariable("id") Object id) throws ReservationsException {
        ScheduleDto scheduleDto = null;
        if (scheduleService instanceof ScheduleServiceJpa) {
            scheduleDto = (ScheduleDto) scheduleService.findById(Long.parseLong((String)id));
        }
        if (scheduleService instanceof ScheduleServiceMongo) {
            scheduleDto = (ScheduleDto) scheduleService.findById((String)id);
        }
        if (scheduleDto != null) {
            return new ResponseEntity<>(scheduleDto, HttpStatus.FOUND);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable("id") Object id, @RequestBody ScheduleDto scheduleDto) throws ReservationsException {
        if (scheduleService instanceof ScheduleServiceJpa) {
            scheduleService.update(scheduleDto, Long.parseLong((String)id));
        }
        if (scheduleService instanceof ScheduleServiceMongo) {
            scheduleService.update(scheduleDto, (String)id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Object id) throws ReservationsException {
        if (scheduleService instanceof ScheduleServiceJpa) {
            scheduleService.deleteById(Long.parseLong((String)id));
        }
        if (scheduleService instanceof ScheduleServiceMongo) {
            scheduleService.deleteById((String)id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
