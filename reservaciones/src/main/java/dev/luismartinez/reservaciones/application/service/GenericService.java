package dev.luismartinez.reservaciones.application.service;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, Object> {

    T save(T entity) throws ReservationsException;
    T findById(Object id) throws ReservationsException;
    List<T> findAll();
    void deleteById(Object id) throws ReservationsException;

    void update(T entity, Object id)  throws ReservationsException;
}
