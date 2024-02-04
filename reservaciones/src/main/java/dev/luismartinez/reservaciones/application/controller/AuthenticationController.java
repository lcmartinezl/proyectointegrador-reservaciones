package dev.luismartinez.reservaciones.application.controller;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.service.security.AuthenticationService;
import dev.luismartinez.reservaciones.domain.dto.AuthenticationDto;
import dev.luismartinez.reservaciones.domain.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthenticationController(
        AuthenticationService authenticationService
) {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        String token = authenticationService.register(userDto);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authenticationDto) throws ReservationsException {
        String token = authenticationService.login(authenticationDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
