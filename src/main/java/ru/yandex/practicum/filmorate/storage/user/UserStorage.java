package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findUsers();

    Optional<User> findUserById(Long id);

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> addFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    Optional<User> alienateFriends(Long userId, Long friendId);

    List<User> getCommonFriends(Long user1Id, Long user2Id);
}
