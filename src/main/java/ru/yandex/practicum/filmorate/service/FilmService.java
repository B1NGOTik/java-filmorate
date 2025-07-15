package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findFilms() {
        return filmStorage.findFilms();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(Long filmId, Long userId) {
        if (userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Невозможно поставить лайк, так как пользователя с таким id не найдено");
        }
        Film film = findFilmById(filmId);
        film.addLike(userId);
        filmStorage.updateFilm(film);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        if (userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Невозможно убрать лайк, так как пользователя с таким id не найдено");
        }
        Film film = findFilmById(filmId);
        film.removeLike(userId);
        filmStorage.updateFilm(film);
        return film;
    }

    public List<Film> getPopularFilms(Long size) {
        return findFilms()
                .stream()
                .sorted((film1, film2) -> {
                    return film2.getLikes().size() - film1.getLikes().size();
                })
                .limit(size)
                .collect(Collectors.toList());
    }
}
