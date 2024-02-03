package dev.luismartinez.reservaciones.application.service.schedule;

import dev.luismartinez.reservaciones.application.exception.ReservationsException;
import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.domain.dto.ScheduleDto;
import dev.luismartinez.reservaciones.domain.entity.jpa.Schedule;
import dev.luismartinez.reservaciones.domain.repository.jpa.ScheduleRepositoryJpa;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public record ScheduleServiceJpa(
        ScheduleRepositoryJpa scheduleRepository
) implements ScheduleGenericSerive<ScheduleDto, Long>{

    public ScheduleDto save(ScheduleDto scheduleDto)  throws ReservationsException {
        this.validateSchedule(scheduleDto, (long) -1);
        Schedule schedule = new Schedule(
            scheduleDto.id(), scheduleDto.dayOfWeek(),
                scheduleDto.initTime(),scheduleDto.finishTime());
        Schedule schedule1 = scheduleRepository.save(schedule);
        return new ScheduleDto(
                schedule1.getId(),
                schedule1.getDayOfWeek(),
                schedule1.getInitTime(),
                schedule1.getFinishTime()
        );
    }
    public ScheduleDto findById(Long id) throws ReservationsException {
        Optional<Schedule> schedule = scheduleRepository.findById(id);

        if (schedule.isPresent()) {
            return new ScheduleDto(
                    schedule.get().getId(),
                    schedule.get().getDayOfWeek(),
                    schedule.get().getInitTime(),
                    schedule.get().getFinishTime()
            );
        } else {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
    }

    public List<ScheduleDto> findAll() {
        List<Schedule> list = scheduleRepository.findAllByOrderByDayOfWeekAsc();

        List<ScheduleDto> listDto = new ArrayList<>();
        for (Schedule s: list) {
            listDto.add(new ScheduleDto(
                    s.getId(),
                    s.getDayOfWeek(),
                    s.getInitTime(),
                    s.getFinishTime()
            ));
        }
        return listDto;
    }

    public void update(ScheduleDto scheduleDto,Long id) throws ReservationsException {
        Optional<Schedule> schedule1 = scheduleRepository.findById(id);
        if (!schedule1.isPresent()) {
            throw new ReservationsException(EMessage.SCHEDULE_NOT_FOUND);
        }
        this.validateSchedule(scheduleDto, id);
        Schedule schedule = new Schedule(
                id, scheduleDto.dayOfWeek(),
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
        if (scheduleDto.finishTime().isBefore(scheduleDto.initTime())) {
            throw new ReservationsException(EMessage.SCHEDULE_INVALID_TIME_RANGE);
        }


        if (id == -1) {
            List<Schedule> listDay = scheduleRepository.findByDayOfWeekOrderByInitTimeAsc(scheduleDto.dayOfWeek());
            // Validar que no hayan dos registros con el mismo dia
            if (!listDay.isEmpty()) {
                throw new ReservationsException(EMessage.SCHEDULE_SAME_DAYOFWEEK);
            }
        } else {
            List<Schedule> listDay = scheduleRepository.findByDayOfWeekAndIdNotOrderByInitTimeAsc(scheduleDto.dayOfWeek(),id);
            // Validar que no hayan dos registros con el mismo dia
            if (!listDay.isEmpty()) {
                throw new ReservationsException(EMessage.SCHEDULE_SAME_DAYOFWEEK);
            }
        }



    }



}
