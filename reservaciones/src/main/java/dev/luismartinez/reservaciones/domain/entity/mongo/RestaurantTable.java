package dev.luismartinez.reservaciones.domain.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tables")
public class RestaurantTable {
    @Id
    private String id;
    private String name;
    private Integer totalSeats;
}
