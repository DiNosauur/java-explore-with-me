package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; // Заголовок
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание события
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description; // Полное описание события
    private Long category; // Категория события
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private LocationDto location; // Координаты события
    private Boolean paid; // Нужно ли оплачивать участие
    private Long participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
}
