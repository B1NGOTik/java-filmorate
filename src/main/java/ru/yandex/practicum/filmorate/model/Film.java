package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private Mpa mpa = new Mpa();
    private List<Genre> genres;
    private Set<Long> likes;

    public void setMpaId(Long id) {
        mpa.setId(id);
    }

    public Long addLike(Long id) {
        likes.add(id);
        return id;
    }

    public Long removeLike(Long id) {
        likes.remove(id);
        return id;
    }

}
