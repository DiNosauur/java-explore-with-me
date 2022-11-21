package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {
    private long id; // уникальный идентификатор пользователя;
    private String name; // имя или логин пользователя;
}
