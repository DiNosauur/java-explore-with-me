package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private long id; // уникальный идентификатор категории;
    private String name; // имя категории;
}
