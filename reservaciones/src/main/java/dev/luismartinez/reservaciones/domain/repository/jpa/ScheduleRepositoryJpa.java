package dev.luismartinez.reservaciones.domain.repository.jpa;

import dev.luismartinez.reservaciones.domain.entity.jpa.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryJpa extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDayOfWeekOrderByInitTimeAsc(DayOfWeek dayNumber);

    List<Schedule> findByDayOfWeekAndIdNotOrderByInitTimeAsc(DayOfWeek dayNumber, Long id);

    List<Schedule> findAllByOrderByDayOfWeekAsc();

    Optional<Schedule> findByDayOfWeekAndInitTimeAndFinishTime(DayOfWeek dayOfWeek, LocalTime initTime, LocalTime finishTime);

    Optional<Schedule> findOneByDayOfWeek(DayOfWeek dayNumber);
}

