package ru.practicum.ewm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiError {
    private final List<String> errors; // Список стектрейсов или описания ошибок
    private final String message; // Сообщение об ошибке
    private final String reason; // Общее описание причины ошибки
    private final String status; // Код статуса HTTP-ответа
    private final String timestamp; // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
}
