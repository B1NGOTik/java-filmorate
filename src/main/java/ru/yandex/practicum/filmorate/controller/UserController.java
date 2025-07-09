package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    HashMap<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрошен список всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
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

    @PutMapping
    public User update(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Данные пользователя с id {} обновлены", user.getId());
            return user;
        } else {
            log.warn("Пользователя с id {} нету", user.getId());
            throw new ValidationException(String.format("Пользователя с id %d не существует", user.getId()));
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
