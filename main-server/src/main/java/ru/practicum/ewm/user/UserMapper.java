package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserRequestDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setName(userRequestDto.getName());
        return user;
    }
}
