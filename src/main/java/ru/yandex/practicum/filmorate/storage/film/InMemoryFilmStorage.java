package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Getter
    private HashMap<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findFilms() {
        log.info("Запрошен список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Имя не может быть пустым");
            throw new ValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания не может быть больше 200 символов");
            throw new ValidationException("Длина описания не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выпуска не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата выпуска не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Записан фильм с названием {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Данные фильма с id {} обновлены", film.getId());
            return film;
        } else {
            log.warn("Фильма с id {} нет в списке", film.getId());
            throw new NotFoundException(String.format("Фильма с id %d нет в списке", film.getId()));
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
