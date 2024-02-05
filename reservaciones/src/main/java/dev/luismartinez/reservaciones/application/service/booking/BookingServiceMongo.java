package dev.luismartinez.reservaciones.application.service.booking;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.AvailabiltyDto;
import dev.luismartinez.reservaciones.domain.dto.BookingDto;
import dev.luismartinez.reservaciones.domain.entity.mongo.Booking;
import dev.luismartinez.reservaciones.domain.entity.mongo.RestaurantTable;
import dev.luismartinez.reservaciones.domain.entity.mongo.Schedule;
import dev.luismartinez.reservaciones.domain.repository.mongo.BookingRepositoryMongo;
import dev.luismartinez.reservaciones.domain.repository.mongo.RestaurantTableRepositoryMongo;
import dev.luismartinez.reservaciones.domain.repository.mongo.ScheduleRepositoryMongo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public record BookingServiceMongo(
        BookingRepositoryMongo bookingRepository,
        RestaurantTableRepositoryMongo tableRepository,
        ScheduleRepositoryMongo scheduleRepository
) implements BookinkGenericService<BookingDto,String> {

    @Override
    public BookingDto save(BookingDto dto) throws ReservationsException {

        Optional<RestaurantTable> table = tableRepository.findById((String) dto.tableId());
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        System.out.println("create booking");
        System.out.println(dto);
        validateBooking(dto, table.get(), "");

        Booking booking = Booking.builder()
                .initDate(dto.initDate())
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
    public BookingDto findById(String id) throws ReservationsException {
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
    public void deleteById(String id) throws ReservationsException {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NOT_FOUND);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public void update(BookingDto dto, String id) throws ReservationsException {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NOT_FOUND);
        }

        Optional<RestaurantTable> table = tableRepository.findById((String)dto.tableId());
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }

        validateBooking(dto, table.get(), id);

        Booking b = Booking.builder()
                .id(id)
                .initDate(dto.initDate())
                .finishDate(dto.initDate().plusMinutes(dto.minutesDuration()))
                .table(table.get())
                .customerName(dto.customerName())
                .customerEmail(dto.customerEmail())
                .customerPhoneNumber(dto.customerPhoneNumber())
                .peopleNumber(dto.peopleNumber())
                .build();
        bookingRepository.save(b);
    }



    private void validateBooking(BookingDto bookingDto, RestaurantTable table, String id) throws ReservationsException {
        // Validar que si haya un Schedule definido el dia de la fecha del booking
        Optional<Schedule> schedule = scheduleRepository.findOneByDayOfWeek(bookingDto.initDate().getDayOfWeek());
        if (!schedule.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        if (bookingDto.peopleNumber()>table.getTotalSeats()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SEATS_AVAILABLE);
        }
        LocalDateTime initialDate = bookingDto.initDate();
        LocalDateTime finalDate = bookingDto.initDate().plusMinutes(bookingDto.minutesDuration());

        if (initialDate.toLocalTime().isBefore(schedule.get().getInitTime())) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        if (finalDate.toLocalTime().isAfter(schedule.get().getFinishTime())) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }

        LocalDate date = bookingDto.initDate().toLocalDate();
        System.out.println(date.atStartOfDay());
        System.out.println(date.atStartOfDay().plusHours(23).plusMinutes(59));
        List<Booking> bl = bookingRepository.findByTableAndInitDateBetweenOrderByInitDateAsc(table, date.atStartOfDay(), date.atStartOfDay().plusHours(23).plusMinutes(59));
        for (Booking b: bl) {
            if (b.getId().equals(id)) {
                continue;
            }
            //System.out.println(b);
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

    public List<AvailabiltyDto> findAvailability(String tableId, LocalDate date) throws ReservationsException {
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        if (!table.isPresent()) {
            throw new ReservationsException(EMessage.TABLE_NOT_FOUND);
        }
        Optional<Schedule> schedule = scheduleRepository.findOneByDayOfWeek(date.getDayOfWeek());
        if (!schedule.isPresent()) {
            throw new ReservationsException(EMessage.RESERVATION_NO_SCHEDULE_FOUND);
        }
        List<AvailabiltyDto> list = new ArrayList<>();

        LocalDateTime initialDate = LocalDateTime.of(date, schedule.get().getInitTime());
        LocalDateTime finalDate = LocalDateTime.of(date, schedule.get().getFinishTime());
        boolean cicle = true;
        while (cicle) {
            List<Booking> bookingList =  bookingRepository.findByTableAndInitDateGreaterThanEqualAndFinishDateLessThanOrderByInitDateAsc(table.get(), initialDate, finalDate);
            if (!bookingList.isEmpty()) {
                Booking b = bookingList.get(0);
                if (b.getInitDate().equals(initialDate)) {
                    list.add(new AvailabiltyDto(
                            b.getInitDate(),
                            b.getFinishDate(),
                            true,
                            b.getCustomerName()
                    ));
                }
                if (b.getInitDate().isAfter(initialDate)) {
                    // Agregar espacio disponible
                    list.add(new AvailabiltyDto(
                            initialDate,
                            b.getInitDate(),
                            false,
                            ""
                    ));
                    // Agregar booking reservado
                    list.add(new AvailabiltyDto(
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
            list.add(new AvailabiltyDto(
                    initialDate,
                    finalDate,
                    false,
                    ""
            ));
        }

        return list;
    }
}
