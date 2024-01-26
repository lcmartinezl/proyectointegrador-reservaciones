package dev.luismartinez.reservaciones.application.service;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import dev.luismartinez.reservaciones.domain.entity.Schedule;
import dev.luismartinez.reservaciones.domain.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public record ScheduleService(
        ScheduleRepository scheduleRepository
) {

    public ScheduleDto createSchedule(ScheduleDto scheduleDto)  throws ReservationsException {
        this.validateSchedule(scheduleDto, (long) -1);
        Schedule schedule = new Schedule(
            scheduleDto.id(), scheduleDto.description(),scheduleDto.dayOfWeek(),
                scheduleDto.initTime(),scheduleDto.finishTime());
        Schedule schedule1 = scheduleRepository.save(schedule);
        return new ScheduleDto(
                schedule1.getId(),
                schedule1.getDescription(),
                schedule1.getDayOfWeek(),
                schedule1.getInitTime(),
                schedule1.getFinishTime()
        );
    }
    public ScheduleDto findScheduleById(Long id) throws ReservationsException {
        Optional<Schedule> schedule = scheduleRepository.findById(id);

        if (schedule.isPresent()) {
            return new ScheduleDto(
                    schedule.get().getId(),
                    schedule.get().getDescription(),
                    schedule.get().getDayOfWeek(),
                    schedule.get().getInitTime(),
                    schedule.get().getFinishTime()
            );
        } else {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
    }

    public List<ScheduleDto> findAllSchedules() throws ReservationsException{
        List<Schedule> list = scheduleRepository.findAll();
        if (list.isEmpty()) {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
        List<ScheduleDto> listDto = new ArrayList<>();
        for (Schedule s: list) {
            listDto.add(new ScheduleDto(
                    s.getId(),
                    s.getDescription(),
                    s.getDayOfWeek(),
                    s.getInitTime(),
                    s.getFinishTime()
            ));
        }
        return listDto;
    }

    public List<ScheduleDto> findScheduleByDayNumber(DayOfWeek dayNumber) throws ReservationsException {
        List<Schedule> list = scheduleRepository.findByDayOfWeekOrderByInitTimeAsc(dayNumber);
        if (list.isEmpty()) {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
        List<ScheduleDto> listDto = new ArrayList<>();
        for (Schedule s: list) {
            listDto.add(new ScheduleDto(
                    s.getId(),
                    s.getDescription(),
                    s.getDayOfWeek(),
                    s.getInitTime(),
                    s.getFinishTime()
            ));
        }
        return listDto;
    }

    public void updateSchedule(Long id, ScheduleDto scheduleDto) throws ReservationsException {
        Optional<Schedule> schedule1 = scheduleRepository.findById(id);
        if (!schedule1.isPresent()) {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
        this.validateSchedule(scheduleDto, id);
        Schedule schedule = new Schedule(
                id, scheduleDto.description(),scheduleDto.dayOfWeek(),
                scheduleDto.initTime(),scheduleDto.finishTime());
        scheduleRepository.save(schedule);

    }

    public void deleteById(Long id)  throws ReservationsException {
        Optional<Schedule> schedule1 = scheduleRepository.findById(id);
        if (!schedule1.isPresent()) {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteById(id);

    }

    private void validateSchedule(ScheduleDto scheduleDto, Long id)  throws ReservationsException {
        if (MINUTES.between(scheduleDto.initTime(), scheduleDto.finishTime())<30) {
            throw new ReservationsException(EMessage.SCHEDULE_INVALID_MINIMUN_RANGE);
        }

        if (scheduleDto.finishTime().isBefore(scheduleDto.initTime())) {
            throw new ReservationsException(EMessage.SCHEDULE_INVALID_TIME_RANGE);
        }
        List<Schedule> listDay = scheduleRepository.findByDayOfWeekOrderByInitTimeAsc(scheduleDto.dayOfWeek());

        for (Schedule s : listDay) {
            if (s.getId() == id) {
                continue;
            }
            if ((scheduleDto.initTime().isAfter(s.getInitTime()) || scheduleDto.initTime().equals(s.getInitTime()))
                && scheduleDto.initTime().isBefore(s.getFinishTime())) {
                throw new ReservationsException(EMessage.SCHEDULE_SAME_DAYTIME);
            }
            if ((scheduleDto.finishTime().isAfter(s.getInitTime()) || scheduleDto.initTime().equals(s.getInitTime()))
                        && scheduleDto.finishTime().isBefore(s.getFinishTime())) {
               throw new ReservationsException(EMessage.SCHEDULE_SAME_DAYTIME);
            }
        }

    }

}
