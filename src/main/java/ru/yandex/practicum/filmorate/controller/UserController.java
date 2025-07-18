package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final String friendsPath = "/{id}/friends";
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findUsers();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping(friendsPath + "/{friendId}")
    public List<User> makeFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.makeFriends(id, friendId);
    }

    @DeleteMapping(friendsPath + "/{friendId}")
    public List<User> alienateFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.alienateFriends(id, friendId);
    }

    @GetMapping(friendsPath)
    public List<User> findFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping(friendsPath + "/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
