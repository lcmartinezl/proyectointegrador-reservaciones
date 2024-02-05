package dev.luismartinez.reservaciones.application.service.user;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.UserDto;
import dev.luismartinez.reservaciones.domain.entity.jpa.User;
import dev.luismartinez.reservaciones.domain.repository.jpa.UserRepositoryJpa;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public record UserServiceJpa(
        UserRepositoryJpa repository
) implements UserGenericService<UserDto, Long> {


    @Override
    public UserDto save(UserDto dto)  {
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .role(dto.role())
                .build();
        User saved = repository.save(user);
        return new UserDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getRole()
                );
    }

    @Override
    public UserDto findById(Long id) throws ReservationsException {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new ReservationsException(EMessage.USER_NOT_FOUND);
        }
        return new UserDto(
                user.get().getId(),
                user.get().getName(),
                user.get().getEmail(),
                user.get().getPassword(),
                user.get().getRole()
        );
    }

    @Override
    public List<UserDto> findAll() {
        List<User> list = repository.findAll();
        List<UserDto> dtoList = new ArrayList<>();
        for (User user: list) {
            dtoList.add(
                    new UserDto(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole()
                    )
            );
        }
        return dtoList;
    }

    @Override
    public void deleteById(Long id) throws ReservationsException {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new ReservationsException(EMessage.USER_NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Override
    public void update(UserDto dto, Long id) throws ReservationsException {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new ReservationsException(EMessage.USER_NOT_FOUND);
        }
        User u = User.builder()
                .id(id)
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .role(dto.role())
                .build();
        repository.save(u);
    }
}
