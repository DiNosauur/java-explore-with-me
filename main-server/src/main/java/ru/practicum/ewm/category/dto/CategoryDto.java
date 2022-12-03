package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CategoryDto {
    private long id; // уникальный идентификатор категории;
    @NotBlank(message = "Name is required")
    private String name; // имя категории;
}
