package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    private Long id; // Идентификатор подборки событий
    private List<EventShortDto> events; // Список идентификаторов событий входящих в подборку
    private String title; // Заголовок
    private Boolean pinned; // Закреплена ли подборка на главной странице сайта
}
