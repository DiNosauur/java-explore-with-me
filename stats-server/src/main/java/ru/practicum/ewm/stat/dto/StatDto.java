package ru.practicum.ewm.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatDto {
    private Long id; // Уникальный идентификатор
    private String app; // Название сервиса для которого записывается информация
    private String uri; // URI для которого был осуществлен запрос
    private String ip; // IP-адрес пользователя, осуществившего запрос
    private String timestamp; // Дата и время, когда был совершен запрос к эндпоинту  (в формате "yyyy-MM-dd HH:mm:ss")
}
