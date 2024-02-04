package dev.luismartinez.reservaciones.domain.dto;

import dev.luismartinez.reservaciones.application.lasting.ERole;

public record UserDto  (
        Object id,
        String name,
        String email,
        String password,
        ERole role
)  {

}