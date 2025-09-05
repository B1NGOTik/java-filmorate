package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findFilms();

    Optional<Film> findFilmById(Long id);

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> likeFilm(Long filmId, Long userId);

    Optional<Film> removeLike(Long filmId, Long userId);
}
