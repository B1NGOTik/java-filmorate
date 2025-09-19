package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseRepository {
    private static final String GET_ALL_RATINGS_QUERY = "SELECT * FROM film_ratings";
    private static final String GET_RATING_BY_ID_QUERY = "SELECT * FROM film_ratings WHERE rating_id = ?";
    private static final String GET_COUNT_RATING_ID = "SELECT COUNT(rating_id) FROM film_ratings";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return findMany(GET_ALL_RATINGS_QUERY);
    }

    public Optional<Mpa> getMpaById(Long mpaId) {
        if (mpaId > jdbc.queryForObject(GET_COUNT_RATING_ID, Long.class)) {
            throw new NotFoundException("Рейтинга с таким id не существует");
        }
        return findOne(GET_RATING_BY_ID_QUERY, mpaId);
    }
}
