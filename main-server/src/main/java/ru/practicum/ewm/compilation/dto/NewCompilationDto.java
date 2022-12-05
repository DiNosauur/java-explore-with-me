package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events; // Список идентификаторов событий входящих в подборку
    @NotBlank(message = "Title is required")
    private String title; // Заголовок
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта
}
