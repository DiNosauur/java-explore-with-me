package ru.practicum.ewm.category;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories", schema = "public")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный идентификатор категории;
    @NotBlank(message = "Name is required")
    @Column
    private String name; // имя категории;
}