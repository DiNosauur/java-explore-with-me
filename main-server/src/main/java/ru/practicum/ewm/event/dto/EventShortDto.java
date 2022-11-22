package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventShortDto {
    private long id; // Уникальный идентификатор события
    private String title; // Заголовок
    private String annotation; // Краткое описание события
    private CategoryDto category; // Категория события
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator; // Инициатор события
    private Boolean paid; // Нужно ли оплачивать участие
    private Long views; // Количество просмотрев события
}
