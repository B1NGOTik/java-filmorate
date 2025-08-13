package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private Set<String> genres = new HashSet<>();
    private String rating;
    private Set<Long> likes = new HashSet<>();


    public Long addLike(Long id) {
        likes.add(id);
        return id;
    }

    public Long removeLike(Long id) {
        likes.remove(id);
        return id;
    }
}
