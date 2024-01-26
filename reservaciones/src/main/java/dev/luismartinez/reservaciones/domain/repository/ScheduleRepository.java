package dev.luismartinez.reservaciones.domain.repository;

import dev.luismartinez.reservaciones.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDayOfWeekOrderByInitTimeAsc(DayOfWeek dayNumber);
}
