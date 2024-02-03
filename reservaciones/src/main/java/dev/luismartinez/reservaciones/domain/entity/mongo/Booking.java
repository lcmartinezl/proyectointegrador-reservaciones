package dev.luismartinez.reservaciones.domain.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private LocalDateTime initDate;
    private LocalDateTime finishDate;
    @DBRef
    private RestaurantTable table;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private int peopleNumber;
}
