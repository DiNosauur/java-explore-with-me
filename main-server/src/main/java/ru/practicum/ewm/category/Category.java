package ru.practicum.ewm.category;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "categories", schema = "public")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный идентификатор категории;
    private String name; // имя категории;
}