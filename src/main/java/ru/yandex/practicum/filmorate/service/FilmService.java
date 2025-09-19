package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms();
    }

    public Optional<Film> findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public Optional<Film> createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> likeFilm(Long filmId, Long userId) {
        return filmStorage.likeFilm(filmId, userId);
    }

    public Optional<Film> removeLike(Long filmId, Long userId) {
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Long size) {
        return findFilms()
                .stream()
                .sorted((film1, film2) -> {
                    int size1 = film1.getLikes().size();
                    int size2 = film2.getLikes().size();
                    if (film1.getLikes().isEmpty()) {
                        size1 = 0;
                    }
                    if (film2.getLikes().isEmpty()) {
                        size2 = 0;
                    }
                    return size2 - size1;
                })
                .limit(size)
                .collect(Collectors.toList());
    }
}
