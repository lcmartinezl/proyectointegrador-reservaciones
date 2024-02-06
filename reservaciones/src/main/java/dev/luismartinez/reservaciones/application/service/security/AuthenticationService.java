package dev.luismartinez.reservaciones.application.service.security;


import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.application.lasting.ERole;
import dev.luismartinez.reservaciones.application.service.user.UserGenericService;
import dev.luismartinez.reservaciones.application.service.user.UserServiceMongo;
import dev.luismartinez.reservaciones.domain.dto.AuthenticationDto;
import dev.luismartinez.reservaciones.domain.dto.UserDto;
import dev.luismartinez.reservaciones.domain.repository.jpa.UserRepositoryJpa;
import dev.luismartinez.reservaciones.domain.repository.mongo.UserRepositoryMongo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public record AuthenticationService(
        UserRepositoryMongo userRepositoryMongo,
        UserRepositoryJpa userRepositoryJpa,
        UserGenericService userService,
  JwtService jwtService,
  PasswordEncoder passwordEncoder,
  AuthenticationManager authenticationManager
) {

  public String register(UserDto userDto) {
    if (userService instanceof UserServiceMongo) {
      dev.luismartinez.reservaciones.domain.entity.mongo.User user = dev.luismartinez.reservaciones.domain.entity.mongo.User.builder()
              .name(userDto.name())
              .email(userDto.email())
              .password(passwordEncoder().encode(userDto.password()))
              .role(ERole.USER)
              .build();
      userRepositoryMongo.save(user);
      return jwtService.generateToken(user);
    } else {
      dev.luismartinez.reservaciones.domain.entity.jpa.User user = dev.luismartinez.reservaciones.domain.entity.jpa.User.builder()
              .name(userDto.name())
              .email(userDto.email())
              .password(passwordEncoder().encode(userDto.password()))
              .role(ERole.USER)
              .build();
      userRepositoryJpa.save(user);
      return jwtService.generateToken(user);
    }

  }

  public String login(AuthenticationDto authenticationDto) throws ReservationsException {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        authenticationDto.email(),
        authenticationDto.password()
      )
    );


    if (userService instanceof UserServiceMongo) {
      Optional<dev.luismartinez.reservaciones.domain.entity.mongo.User> user = userRepositoryMongo.findUserByEmail(authenticationDto.email());
      if (!user.isPresent()) {
        throw new ReservationsException(EMessage.USER_NOT_FOUND);
      }
      return jwtService().generateToken(user.get());
    } else {
      Optional<dev.luismartinez.reservaciones.domain.entity.jpa.User> user = userRepositoryJpa.findUserByEmail(authenticationDto.email());
      if (!user.isPresent()) {
        throw new ReservationsException(EMessage.USER_NOT_FOUND);
      }
      return jwtService().generateToken(user.get());
    }
  }
}
