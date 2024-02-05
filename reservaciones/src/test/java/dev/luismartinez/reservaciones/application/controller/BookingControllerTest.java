package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.service.booking.BookingServiceJpa;
import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
import dev.luismartinez.reservaciones.domain.dto.BookingDto;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookingController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingServiceJpa service;

    @Test
    public void givenBooking_whenGetBooking_thenReturnJsonArray() throws Exception {
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
        List<BookingDto> allTables = Arrays.asList(dto);

        given(service.findAll()).willReturn(allTables);

        mvc.perform(get("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerName", is(dto.customerName())));

    }

}
