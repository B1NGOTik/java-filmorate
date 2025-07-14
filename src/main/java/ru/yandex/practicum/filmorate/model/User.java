package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();


    public Long addFriend(Long id) {
        friends.add(id);
        return id;
    }

    public Long deleteFriend(Long id) {
        friends.remove(id);
        return id;
    }
}
