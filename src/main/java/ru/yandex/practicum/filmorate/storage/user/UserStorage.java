package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findUsers();

    User findUserById(Long id);

    User createUser(User user);

    User updateUser(User user);
}
