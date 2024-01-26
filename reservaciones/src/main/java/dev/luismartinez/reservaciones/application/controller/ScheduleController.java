package dev.luismartinez.reservaciones.application.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.ScheduleService;

import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import dev.luismartinez.reservaciones.domain.entity.Schedule;

import dev.luismartinez.reservaciones.exception.ScheduleInvalidTimeRangeException;
import dev.luismartinez.reservaciones.exception.ScheduleNotFoundException;
import dev.luismartinez.reservaciones.exception.SchedulesOnSameDayAndTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schedule")
public record ScheduleController(
    ScheduleService scheduleService
) {

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleDto scheduleDto) throws ReservationsException {
        ScheduleDto dto = scheduleService.createSchedule(scheduleDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllSchedules() throws ReservationsException{
        List<ScheduleDto> list = scheduleService.findAllSchedules();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws ReservationsException {
        ScheduleDto scheduleDto =scheduleService.findScheduleById(id);
        return new ResponseEntity<>(scheduleDto, HttpStatus.FOUND);

    }

    @GetMapping("/findByDayNumber/{dayNumber}")
    public ResponseEntity<?> findScheduleByDayNumber(@PathVariable("dayNumber") Integer dayNumber) throws ReservationsException{
        System.out.println(DayOfWeek.of(dayNumber));
        List<ScheduleDto> list = scheduleService.findScheduleByDayNumber(DayOfWeek.of(dayNumber));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable("id") Long id, @RequestBody ScheduleDto scheduleDto) throws ReservationsException {
        scheduleService.updateSchedule(id, scheduleDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws ReservationsException {
        scheduleService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
