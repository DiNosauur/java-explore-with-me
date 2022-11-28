package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEventRequestDto {
    private Long eventId; // Идентификатор события
    private String title; // Заголовок
    private String annotation; // Краткое описание события
    private String description; // Полное описание события
    private Long category; // Категория события
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Boolean paid; // Нужно ли оплачивать участие
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
}
