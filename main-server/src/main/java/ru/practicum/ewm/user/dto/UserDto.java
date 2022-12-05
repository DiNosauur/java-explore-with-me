package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private long id; // уникальный идентификатор пользователя;
    private String email; // адрес электронной почты;
    private String name; // имя или логин пользователя;
}
