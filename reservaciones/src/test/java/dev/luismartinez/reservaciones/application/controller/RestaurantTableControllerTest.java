package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.service.table.RestaurantTableServiceJpa;
import dev.luismartinez.reservaciones.domain.dto.RestaurantTableDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RestaurantTableController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RestaurantTableControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantTableServiceJpa service;

    @Test
    public void givenTable_whenGetTables_thenReturnJsonArray() throws Exception {
        RestaurantTableDto dto = new RestaurantTableDto(
                null,
                "Mesa 1",
                4

        );
        List<RestaurantTableDto> allTables = Arrays.asList(dto);

        given(service.findAll()).willReturn(allTables);

        mvc.perform(get("/api/v1/table")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(dto.name())));

    }

}
