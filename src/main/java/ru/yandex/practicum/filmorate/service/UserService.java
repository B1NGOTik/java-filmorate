package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public Optional<User> findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public Optional<User> createUser(User user) {
        return userStorage.createUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> addFriend(Long userId, Long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public Optional<User> alienateFriends(Long userId1, Long userId2) {
        return userStorage.alienateFriends(userId1, userId2);
    }

    public List<User> getUserFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

}
