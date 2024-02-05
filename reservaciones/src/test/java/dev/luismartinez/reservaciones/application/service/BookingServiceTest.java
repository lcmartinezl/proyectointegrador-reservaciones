package dev.luismartinez.reservaciones.application.service;

import dev.luismartinez.reservaciones.application.service.booking.BookingServiceJpa;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
import dev.luismartinez.reservaciones.domain.dto.BookingDto;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import dev.luismartinez.reservaciones.domain.entity.jpa.Booking;
import dev.luismartinez.reservaciones.domain.entity.jpa.RestaurantTable;
import dev.luismartinez.reservaciones.domain.entity.jpa.Schedule;
import dev.luismartinez.reservaciones.domain.repository.jpa.BookingRepositoryJpa;
import dev.luismartinez.reservaciones.domain.repository.jpa.ScheduleRepositoryJpa;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingServiceJpa service;

    @MockBean
    private BookingRepositoryJpa repository;

    @Test
    @SneakyThrows
    void testCreateBooking() {
        // Given
        BookingDto dto = new BookingDto(
            null,
            LocalDateTime.of(2024,1,30,15,00),
                (long) 60,
                (long) 1,
                "Luis Martinez",
                "n/a",
                "2432-4333",
                4

        );

        // When
        service.save(dto);

        //Then
        verify(repository).save(any(Booking.class));
    }

    @Test
    @SneakyThrows
    void testFindBookingById() {
        //Given
        final Long id = (long)1;

        BookingDto dto = new BookingDto(
                (long) 1,
                LocalDateTime.of(2024,1,30,15,00),
                (long) 60,
                (long) 1,
                "Luis Martinez",
                "n/a",
                "2432-4333",
                4

        );

        RestaurantTable table =RestaurantTable.builder()
                .id((long)1)
                .name("Mesa 1")
                .totalSeats(4)
                .build();

        Booking expected =Booking.builder()
                .id((long)1)
                .initDate(LocalDateTime.of(2024,1,30,15,00))
                .finishDate(LocalDateTime.of(2024,1,30,16,00))
                .table(table)
                .customerName("Luis Martinez")
                .customerEmail("n/a")
                .customerPhoneNumber("4344-4444")
                .peopleNumber(4)
                .build();

        // When
        when(repository.findById(id)).thenReturn(Optional.of(expected));
        BookingDto resultDto = service.findById(id);

        // Then
        assertEquals(expected, resultDto);
    }
}
