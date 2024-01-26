package dev.luismartinez.reservaciones.domain.service;

import dev.luismartinez.reservaciones.domain.entity.Appointment;
import dev.luismartinez.reservaciones.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AppointmentServicePostgreSQL implements AppointmentService{

    private final AppointmentRepository appointmentRepository;

    public AppointmentServicePostgreSQL(@Autowired AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public Appointment save(Appointment entity) {
        return appointmentRepository.save(entity);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public void delete(Appointment entity) {
        appointmentRepository.delete(entity);
    }
}
