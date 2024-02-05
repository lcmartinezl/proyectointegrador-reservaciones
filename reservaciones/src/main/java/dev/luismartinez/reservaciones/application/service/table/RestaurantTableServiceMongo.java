package dev.luismartinez.reservaciones.application.service.table;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.RestaurantTableDto;
import dev.luismartinez.reservaciones.domain.entity.mongo.RestaurantTable;
import dev.luismartinez.reservaciones.domain.repository.mongo.RestaurantTableRepositoryMongo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public record RestaurantTableServiceMongo(
        RestaurantTableRepositoryMongo repository
) implements  RestaurantTableGenericService <RestaurantTableDto, String> {

    @Override
    public RestaurantTableDto save(RestaurantTableDto dto) {
        RestaurantTable table = RestaurantTable.builder()
                .name(dto.name())
                .totalSeats(dto.totalSeats())
                .build();
        RestaurantTable saved = repository.save(table);
        return new RestaurantTableDto(saved.getId(), saved.getName(), saved.getTotalSeats());
    }

    @Override
    public RestaurantTableDto findById(String id) throws ReservationsException {
        //Validar que exista la mesa con el id
        Optional<RestaurantTable> table = repository.findById(id);
        if (table.isPresent()) {
            return new RestaurantTableDto(table.get().getId(), table.get().getName(), table.get().getTotalSeats());
        } else {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }

    }

    @Override
    public List<RestaurantTableDto> findAll() {
        List<RestaurantTable> list = repository.findAll();
        List<RestaurantTableDto> listDto = new ArrayList<>();
        for (RestaurantTable table:list) {
            listDto.add(new RestaurantTableDto(table.getId(), table.getName(), table.getTotalSeats()));
        }
        return listDto;
    }

    @Override
    public void deleteById(String id) throws ReservationsException {
        //Validar que exista la mesa con el id
        Optional<RestaurantTable> table = repository.findById(id);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Override
    public void update(RestaurantTableDto dto, String id) throws ReservationsException {
        //Validar que exista la mesa con el id
        Optional<RestaurantTable> table = repository.findById(id);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        RestaurantTable t = RestaurantTable.builder()
                .id(id)
                .name(dto.name())
                .totalSeats(dto.totalSeats())
                .build();
        repository.save(t);
    }
}
