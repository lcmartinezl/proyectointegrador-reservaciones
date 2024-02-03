package dev.luismartinez.reservaciones.domain.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "schedules")
public class Schedule {
    @Id
    private String id;
    private DayOfWeek dayOfWeek;
    private LocalTime initTime;
    private LocalTime finishTime;
}
