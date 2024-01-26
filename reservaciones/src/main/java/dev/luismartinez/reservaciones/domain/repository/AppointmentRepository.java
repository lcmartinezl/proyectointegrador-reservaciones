package dev.luismartinez.reservaciones.domain.repository;

import dev.luismartinez.reservaciones.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
