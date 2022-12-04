package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Collection<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.info("Получение информации о пользователях (ids={})", ids);
        int page = from / size;
        return repository.getUsers(
                ids == null ? 0 : 1,
                ids,
                PageRequest.of(page, size))
                .stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    private void validate(User user) {
        Optional<User> foundUser = repository.findByEmail(user.getEmail());
        if (foundUser.isPresent() && foundUser.get().getId() != user.getId()) {
            throw new ConflictException("Пользователь с таким email уже зарегестрирован");
        }
        foundUser = repository.findByName(user.getName());
        if (foundUser.isPresent() && foundUser.get().getId() != user.getId()) {
            throw new ConflictException("Пользователь с таким именем уже зарегестрирован");
        }
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        log.info("Добавление пользователя {}", user.toString());
        validate(user);
        return repository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> updateUser(User user) {
        log.info("Редактирование пользователя {}", user.toString());
        validate(user);
        Optional<User> changeableUser = getUser(user.getId());
        if (changeableUser.isPresent()) {
            if (user.getEmail() != null) {
                changeableUser.get().setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                changeableUser.get().setName(user.getName());
            }
            repository.save(changeableUser.get());
        }
        return changeableUser;
    }

    @Transactional
    @Override
    public boolean deleteUser(long id) {
        log.info("Удаление пользователя (id={})", id);
        Optional<User> user = getUser(id);
        if (user.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        return repository.findById(id);
    }
}
