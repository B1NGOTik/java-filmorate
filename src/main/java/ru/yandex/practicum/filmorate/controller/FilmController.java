package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final String likePath = "/{id}/like/{userId}";
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findFilms();
    }

    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping(likePath)
    public Film likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping(likePath)
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findFilmByPopularity(@RequestParam(defaultValue = "10") Long size) {
        return filmService.getPopularFilms(size);
    }


}
