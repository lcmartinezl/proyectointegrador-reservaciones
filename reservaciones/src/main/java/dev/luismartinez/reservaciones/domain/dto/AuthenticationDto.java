package dev.luismartinez.reservaciones.domain.dto;

public record AuthenticationDto(
  String email,
  String password
) {
}
