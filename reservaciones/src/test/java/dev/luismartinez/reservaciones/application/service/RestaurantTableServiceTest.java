package dev.luismartinez.reservaciones.application.service;

import dev.luismartinez.reservaciones.application.service.table.RestaurantTableGenericService;
import dev.luismartinez.reservaciones.application.service.table.RestaurantTableServiceJpa;
import dev.luismartinez.reservaciones.domain.dto.RestaurantTableDto;
import dev.luismartinez.reservaciones.domain.entity.jpa.RestaurantTable;
import dev.luismartinez.reservaciones.domain.repository.jpa.RestaurantTableRepositoryJpa;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class RestaurantTableServiceTest {

    @Autowired
    private RestaurantTableServiceJpa tableService;

    @MockBean
    private RestaurantTableRepositoryJpa tableRepository;

    @Test
    @SneakyThrows
    void testCreateRestaurantTable() {
        // Given
        RestaurantTableDto dto = new RestaurantTableDto(
            null,
            "Mesa 1",
            4

        );

        // When
        tableService.save(dto);

        //Then
        verify(tableRepository).save(any(RestaurantTable.class));
    }

    @Test
    @SneakyThrows
    void testFindRestaurantTableById() {
        //Given
        final Long id = (long)1;

        RestaurantTableDto expectedDto = new RestaurantTableDto(
                (long) 1,
                "Mesa 1",
                4

        );

        RestaurantTable expectedTable =RestaurantTable.builder()
                .id((long)1)
                .name("Mesa 1")
                .totalSeats(4)
                .build();

        // When
        when(tableRepository.findById(id)).thenReturn(Optional.of(expectedTable));
        RestaurantTableDto resultDto = tableService.findById(id);

        // Then
        assertEquals(expectedDto, resultDto);
    }
}
