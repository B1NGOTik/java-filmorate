package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    GenreDbStorage storage;

    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Optional<Genre> getGenreById(Long genreId) {
        return storage.getGenreById(genreId);
    }
}
