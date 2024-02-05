package dev.luismartinez.reservaciones.application.service;

import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;

import dev.luismartinez.reservaciones.domain.entity.jpa.Schedule;
import dev.luismartinez.reservaciones.domain.repository.jpa.ScheduleRepositoryJpa;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleServiceJpa service;

    @MockBean
    private ScheduleRepositoryJpa repository;

    @Test
    @SneakyThrows
    void testCreateSchedule() {
        // Given
        ScheduleDto dto = new ScheduleDto(
            null,
            DayOfWeek.MONDAY,
            LocalTime.of(14,00,00),
                LocalTime.of(17,00,00)

        );

        // When
        service.save(dto);

        //Then
        verify(repository).save(any(Schedule.class));
    }

    @Test
    @SneakyThrows
    void testFindScheduleById() {
        //Given
        final Long id = (long)1;

        ScheduleDto dto = new ScheduleDto(
                (long) 1,
                DayOfWeek.MONDAY,
                LocalTime.of(14,00,00),
                LocalTime.of(17,00,00)

        );

        Schedule expected =Schedule.builder()
                .id((long)1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .initTime(LocalTime.of(14,00,00))
                .finishTime(LocalTime.of(17,00,00))
                .build();

        // When
        when(repository.findById(id)).thenReturn(Optional.of(expected));
        ScheduleDto resultDto = service.findById(id);

        // Then
        assertEquals(expected, resultDto);
    }
}
