package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.service.schedule.ScheduleServiceJpa;
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
@WebMvcTest(value = ScheduleController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleServiceJpa service;

    @Test
    public void givenSchedule_whenGetSchedules_thenReturnJsonArray() throws Exception {
        ScheduleDto dto = new ScheduleDto(
                null,
                DayOfWeek.MONDAY,
                LocalTime.of(14,00,00),
                LocalTime.of(17,00,00)

        );
        List<ScheduleDto> allTables = Arrays.asList(dto);

        given(service.findAll()).willReturn(allTables);

        mvc.perform(get("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dayOfWeek", is(dto.dayOfWeek())));

    }

}
