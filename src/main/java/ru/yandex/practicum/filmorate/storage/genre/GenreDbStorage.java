package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository("genreDbStorage")
public class GenreDbStorage extends BaseRepository {
    private final static String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private final static String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private final static String GET_GENRES_COUNT_QUERY = "SELECT COUNT(genre_id) FROM genres";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAllGenres() {
        return findMany(GET_ALL_GENRES_QUERY);
    }

    public Optional<Genre> getGenreById(Long genreId) {
        if (genreId > jdbc.queryForObject(GET_GENRES_COUNT_QUERY, Long.class)) {
            throw new NotFoundException("Жанра с таким id не существует");
        }
        return findOne(GET_GENRE_BY_ID_QUERY, genreId);
    }
}
