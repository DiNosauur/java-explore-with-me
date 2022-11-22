package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventFullDto {
    private long id; // Уникальный идентификатор события
    private String title; // Заголовок
    private String annotation; // Краткое описание события
    private String description; // Полное описание события
    private CategoryDto category; // Категория события
    private String createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator; // Инициатор события
    private LocationDto location; // Координаты события
    private Boolean paid; // Нужно ли оплачивать участие
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private String publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    private EventState state; // статус события
    private Long views; // Количество просмотрев события
}
