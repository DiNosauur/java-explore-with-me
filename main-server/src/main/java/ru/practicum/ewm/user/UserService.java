package ru.practicum.ewm.user;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Collection<UserDto> getUsers(List<Long> ids, int from, int size);

    User saveUser(User user);

    Optional<User> updateUser(User user);

    boolean deleteUser(long id);

    Optional<User> getUser(long id);
}
