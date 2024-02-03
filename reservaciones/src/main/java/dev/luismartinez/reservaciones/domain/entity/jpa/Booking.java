package dev.luismartinez.reservaciones.domain.entity.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime initDate;
    private LocalDateTime finishDate;
    @ManyToOne
    @JoinColumn(name="table_id")
    private RestaurantTable table;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private int peopleNumber;
}
