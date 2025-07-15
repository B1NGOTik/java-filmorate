package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findUsers() {
        return userStorage.findUsers();
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> makeFriends(Long userId1, Long userId2) {
        User user1 = findUserById(userId1);
        User user2 = findUserById(userId2);
        user1.addFriend(userId2);
        user2.addFriend(userId1);
        userStorage.updateUser(user1);
        userStorage.updateUser(user2);
        return List.of(user1, user2);
    }

    public List<User> alienateFriends(Long userId1, Long userId2) {
        User user1 = findUserById(userId1);
        User user2 = findUserById(userId2);
        user1.deleteFriend(userId2);
        user2.deleteFriend(userId1);
        userStorage.updateUser(user1);
        userStorage.updateUser(user2);
        return List.of(user1, user2);
    }

    public List<User> getUserFriends(Long userId) {
        User user = findUserById(userId);
        return user.getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        User user1 = findUserById(userId1);
        User user2 = findUserById(userId2);
        List<Long> commonFriends = new ArrayList<>();
        for (Long friendId : user1.getFriends()) {
            if (user2.getFriends().contains(friendId)) {
                commonFriends.add(friendId);
            }
        }
        return commonFriends.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

}
