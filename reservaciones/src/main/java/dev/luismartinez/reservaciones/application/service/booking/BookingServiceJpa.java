package dev.luismartinez.reservaciones.application.service.booking;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.Availability;
import dev.luismartinez.reservaciones.domain.dto.BookingDto;
import dev.luismartinez.reservaciones.domain.entity.jpa.Booking;
import dev.luismartinez.reservaciones.domain.entity.jpa.RestaurantTable;
import dev.luismartinez.reservaciones.domain.entity.jpa.Schedule;
import dev.luismartinez.reservaciones.domain.repository.jpa.BookingRepositoryJpa;
import dev.luismartinez.reservaciones.domain.repository.jpa.RestaurantTableRepositoryJpa;
import dev.luismartinez.reservaciones.domain.repository.jpa.ScheduleRepositoryJpa;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public record BookingServiceJpa (
    BookingRepositoryJpa bookingRepository,
    RestaurantTableRepositoryJpa  tableRepository,
    ScheduleRepositoryJpa scheduleRepository
) implements BookinkGenericService<BookingDto,Long> {
    @Override
    public BookingDto save(BookingDto dto) throws ReservationsException {
        // Convertir a Long, en dependencia si viene un Int en el dto
        Long tableId = (long)0;
        if (dto.tableId() instanceof Integer) {
            tableId = ((Integer) dto.tableId()).longValue();
        } if (dto.tableId() instanceof Long) {
            tableId = (Long) dto.tableId();
        }
        // Buscar la Mesa a la que aplica la reservacion
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        // Llamada a Validar la reservacion, se manda -1 como segundo parametro,
        // ya que es un booking nuevo que se va a guardar
        validateBooking(dto, table.get(), (long)-1);

        // Si pasa las validaciones (no tira Exception)
        // Construir entidad para guardar
        Booking booking = Booking.builder()
                .initDate(dto.initDate())
                // calcular fecha final de la reservacion
                .finishDate(dto.initDate().plusMinutes(dto.minutesDuration()))
                .table(table.get())
                .customerName(dto.customerName())
                .customerEmail(dto.customerEmail())
                .customerPhoneNumber(dto.customerPhoneNumber())
                .peopleNumber(dto.peopleNumber())
                .build();
        Booking saved = bookingRepository.save(booking);
        return new BookingDto(
                saved.getId(),
                saved.getInitDate(),
                dto.minutesDuration(),
                table.get().getId(),
                saved.getCustomerName(),
                saved.getCustomerEmail(),
                saved.getCustomerPhoneNumber(),
                saved.getPeopleNumber()
                );
    }

    @Override
    public BookingDto findById(Long id) throws ReservationsException {
        // Validar que si exista el Booking, sino Exception
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NOT_FOUND);
        }
        return new BookingDto(
                booking.get().getId(),
                booking.get().getInitDate(),
                ChronoUnit.MINUTES.between(booking.get().getInitDate(), booking.get().getFinishDate()),
                booking.get().getTable().getId(),
                booking.get().getCustomerName(),
                booking.get().getCustomerEmail(),
                booking.get().getCustomerPhoneNumber(),
                booking.get().getPeopleNumber()
        );
    }

    @Override
    public List<BookingDto> findAll() {
        List<Booking> list = bookingRepository.findAllByOrderByInitDateAsc();
        List<BookingDto> listDto = new ArrayList<>();
        for (Booking booking: list) {
            listDto.add(
                    new BookingDto(
                            booking.getId(),
                            booking.getInitDate(),
                            ChronoUnit.MINUTES.between(booking.getInitDate(), booking.getFinishDate()),
                            booking.getTable().getId(),
                            booking.getCustomerName(),
                            booking.getCustomerEmail(),
                            booking.getCustomerPhoneNumber(),
                            booking.getPeopleNumber()
                    )
            );
        }
        return listDto;
    }

    @Override
    public void deleteById(Long id) throws ReservationsException {
        // Validar que si exista el Booking, sino Exception
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NOT_FOUND);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public void update(BookingDto dto, Long id) throws ReservationsException {
        // Validar que si exista el Booking, sino Exception
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NOT_FOUND);
        }
        // Convertir a Long, en dependencia si viene un Int en el dto
        Long tableId = (long)0;
        if (dto.tableId() instanceof Integer) {
            tableId = ((Integer) dto.tableId()).longValue();
        } if (dto.tableId() instanceof Long) {
            tableId = (Long) dto.tableId();
        }
        // Buscar la Mesa a la que aplica la reservacion
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        // Llamada a Validar la reservacion, se manda el id a hacer update,
        validateBooking(dto, table.get(), id);

        Booking b = Booking.builder()
                .id(id)
                .initDate(dto.initDate())
                // calcular fecha final de la reservacion
                .finishDate(dto.initDate().plusMinutes(dto.minutesDuration()))
                .table(table.get())
                .customerName(dto.customerName())
                .customerEmail(dto.customerEmail())
                .customerPhoneNumber(dto.customerPhoneNumber())
                .peopleNumber(dto.peopleNumber())
                .build();
        bookingRepository.save(b);
    }



    private void validateBooking(BookingDto bookingDto, RestaurantTable table, Long id) throws ReservationsException {
        // Validar que si haya un Schedule definido el dia de la fecha del booking
        Optional<Schedule> schedule = scheduleRepository.findOneByDayOfWeek(bookingDto.initDate().getDayOfWeek());
        if (!schedule.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        //Validar que el numero de personas del booking no sobrepasa
        // la cantidad de asientos disponibles para la mesa
        if (bookingDto.peopleNumber()>table.getTotalSeats()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SEATS_AVAILABLE);
        }

        LocalDateTime initialDate = bookingDto.initDate();
        // calcular fecha final de la reservacion
        LocalDateTime finalDate = bookingDto.initDate().plusMinutes(bookingDto.minutesDuration());

        //Validar que la reservacion cumpla con el horario establecido para ese dia de la semana
        if (initialDate.toLocalTime().isBefore(schedule.get().getInitTime())) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        if (finalDate.toLocalTime().isAfter(schedule.get().getFinishTime())) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }

        //Validaciones para buscar traslapes y asi saber cuando el espacio de tiempo
        // de la reservacion ya no esta disponible
        LocalDate date = bookingDto.initDate().toLocalDate();
        // Buscar todas las reservaciones hechas en el mismo dia para la mesa
        List<Booking> bl = bookingRepository.findByTableAndInitDateBetweenOrderByInitDateAsc(table, date.atStartOfDay(), date.atStartOfDay().plusHours(23).plusMinutes(59));
        for (Booking b: bl) {
            //No comparar el la misma entidad en el caso que se esta haciendo update
            if (b.getId() == id) {
                continue;
            }

            if ((bookingDto.initDate().isAfter(b.getInitDate()) || bookingDto.initDate().equals(b.getInitDate()))
                    && bookingDto.initDate().isBefore(b.getFinishDate())) {
                throw new ReservationsException(EMessage.RESERVATION_SAME_DAYTIME);
            }
            if ((finalDate.isAfter(b.getInitDate()) )
                    && (finalDate.isBefore(b.getFinishDate()) || finalDate.equals(b.getFinishDate())  ) ) {
                throw new ReservationsException(EMessage.RESERVATION_SAME_DAYTIME);
            }
            if (bookingDto.initDate().isBefore(b.getInitDate()) && finalDate.isAfter(b.getFinishDate())) {
                throw new ReservationsException(EMessage.RESERVATION_SAME_DAYTIME);
            }
        }
    }

    // ALgoritmo para construir el calendario de reservaciones para una mesa en un dia completo
    // Regresa un List de AvailabiltyDto, cada registro indica el horario en que la mesa esta libre
    // o si ya esta reservada y quien la reservo
    public List<Availability> findAvailability(Long tableId, LocalDate date) throws ReservationsException {
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        Optional<Schedule> schedule = scheduleRepository.findOneByDayOfWeek(date.getDayOfWeek());
        if (!schedule.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        List<Availability> list = new ArrayList<>();

        LocalDateTime initialDate = LocalDateTime.of(date, schedule.get().getInitTime());
        LocalDateTime finalDate = LocalDateTime.of(date, schedule.get().getFinishTime());
        boolean cicle = true;
        while (cicle) {
            List<Booking> bookingList =  bookingRepository.findByTableAndInitDateGreaterThanEqualAndInitDateLessThanOrderByInitDateAsc(table.get(), initialDate, finalDate);
            if (!bookingList.isEmpty()) {
                Booking b = bookingList.get(0);
                if (b.getInitDate().equals(initialDate)) {
                    list.add(new Availability(
                            b.getInitDate(),
                            b.getFinishDate(),
                            true,
                            b.getCustomerName()
                    ));
                }
                if (b.getInitDate().isAfter(initialDate)) {
                    // Agregar espacio disponible
                    list.add(new Availability(
                            initialDate,
                            b.getInitDate(),
                            false,
                            ""
                    ));
                    // Agregar booking reservado
                    list.add(new Availability(
                            b.getInitDate(),
                            b.getFinishDate(),
                            true,
                            b.getCustomerName()
                    ));
                }
                initialDate = b.getFinishDate();
            } else {
                cicle = false;
            }
        }
        if (initialDate.isBefore(finalDate)) {
            list.add(new Availability(
                    initialDate,
                    finalDate,
                    false,
                    ""
            ));
        }

        return list;
    }
}
