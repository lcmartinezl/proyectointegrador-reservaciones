package dev.luismartinez.reservaciones.domain.repository.mongo;

import dev.luismartinez.reservaciones.domain.entity.mongo.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryMongo extends MongoRepository<Schedule, String> {

    List<Schedule> findByDayOfWeekOrderByInitTimeAsc(DayOfWeek dayNumber);
    List<Schedule> findByDayOfWeekAndIdNotOrderByInitTimeAsc(DayOfWeek dayNumber, String id);

    List<Schedule> findAllByOrderByDayOfWeekAsc();
    Optional<Schedule> findOneByDayOfWeek(DayOfWeek dayNumber);
}
