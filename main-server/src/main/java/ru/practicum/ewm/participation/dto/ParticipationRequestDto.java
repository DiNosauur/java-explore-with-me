package ru.practicum.ewm.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id; // Идентификатор заявки на участие в событии
    private LocalDateTime created; // Дата и время создания заявки
    private Long event; // Идентификатор события
    private Long requester; // Идентификатор пользователя, отправившего заявку
    private ParticipationStatus status; // Статус заявки
}
