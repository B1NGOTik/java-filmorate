package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Getter
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Long, User> users = new HashMap<>();

    @Override
    public List<User> findUsers() {
        log.info("Запрошен список всех пользователей");
        return List.copyOf(users.values());
    }

    @Override
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.warn("Некорректный вид email");
            throw new ValidationException("Некорректный вид email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Был создан пользователь с именем {}", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Данные пользователя с id {} обновлены", user.getId());
            return user;
        } else {
            log.warn("Пользователя с id {} нету", user.getId());
            throw new NotFoundException(String.format("Пользователя с id %d не существует", user.getId()));
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
