package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.rating_id FROM films AS f";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(title, description, release_date, duration, rating_id) " +
            "VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(film_id, genre_id) " +
            "VALUES(?,?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET title = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String INSERT_LIKE_ON_FILM_QUERY = "INSERT INTO film_likes(film_id, user_id) " +
            "VALUES(?,?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> findFilms() {
        List<Film> filmsNullGenresAndLikes = findMany(FIND_ALL_QUERY);
        List<Film> filmsWithGenresAndLikes = new ArrayList<>();
        for (Film film : filmsNullGenresAndLikes) {
            film.setGenres(getFilmGenres(film));
            film.setLikes(getFilmLikes(film));
            film.setMpa(getFilmMpa(film.getMpa().getId()));
            filmsWithGenresAndLikes.add(film);
        }
        return filmsWithGenresAndLikes;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        Optional<Film> optionalFilm = findOne(FIND_BY_ID_QUERY, id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            film.setGenres(getFilmGenres(film));
            film.setLikes(getFilmLikes(film));
            film.setMpa(getFilmMpa(film.getMpa().getId()));
            return Optional.of(film);
        } else {
            throw new NotFoundException("Не удалось найти фильм");
        }
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Поле имя должно быть заполнено");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выпуска фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма должна быть больше нуля");
        }
        if (film.getMpa().getId() > getMpaCount()) {
            throw new NotFoundException("Указанного рейтинга не существует");
        }
        Long id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > getGenresCount()) {
                    throw new NotFoundException("Жанра с указанным id не существует");
                }
            }
            Set<Genre> genres = new HashSet<>(film.getGenres());
            for (Genre genre : genres) {
                update(INSERT_FILM_GENRES_QUERY, film.getId(), genre.getId());
            }
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return findFilmById(film.getId());
    }

    @Override
    public Optional<Film> likeFilm(Long filmId, Long userId) {
        Film film = findFilmById(filmId).get();
        film.addLike(userId);
        update(INSERT_LIKE_ON_FILM_QUERY, filmId, userId);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> removeLike(Long filmId, Long userId) {
        update(REMOVE_LIKE_QUERY, filmId, userId);
        return Optional.of(findFilmById(filmId).get());
    }

    private Long getGenresCount() {
        return jdbc.queryForObject("SELECT COUNT(genre_id) FROM genres", Long.class);
    }

    private Long getMpaCount() {
        return jdbc.queryForObject("SELECT COUNT(rating_id) FROM film_ratings", Long.class);
    }

    private List<Genre> getFilmGenres(Film film) {
        List<Long> filmGenresId = jdbc.queryForList("SELECT genre_id FROM film_genres WHERE film_id = ?", Long.class, film.getId());
        List<Genre> filmGenres = new ArrayList<>();
        for (Long genreId : filmGenresId) {
            Genre genre = jdbc.queryForObject("SELECT * FROM genres WHERE genre_id = ?", new GenreRowMapper(), genreId);
            filmGenres.add(genre);
        }
        return filmGenres;
    }


    private Set<Long> getFilmLikes(Film film) {
        return new HashSet<>(jdbc.queryForList("SELECT user_id FROM film_likes WHERE film_id = ?", Long.class, film.getId()));
    }

    private Mpa getFilmMpa(Long mpaId) {
        return jdbc.queryForObject("SELECT * FROM film_ratings WHERE rating_id = ?", new MpaRowMapper(), mpaId);
    }

}
