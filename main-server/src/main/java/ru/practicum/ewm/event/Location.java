package ru.practicum.ewm.event;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "locations", schema = "public")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Уникальный идентификатор
    @Column
    private Double lat; // Широта
    @Column
    private Double lon; // Долгота
}
