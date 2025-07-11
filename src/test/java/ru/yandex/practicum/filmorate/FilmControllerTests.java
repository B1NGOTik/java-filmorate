package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTests {

    private FilmController filmController;
    private InMemoryFilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void create_ValidFilm_ShouldReturnFilm() {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm.getId());
        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
    }

    @Test
    void create_FilmWithEmptyName_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithNullName_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithBlankName_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("   ");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithLongDescription_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("A".repeat(201)); // 201 символов
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithMaxLengthDescription_ShouldNotThrowException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("A".repeat(200)); // Ровно 200 символов
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void create_FilmWithEarlyReleaseDate_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27)); // На день раньше допустимого
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithMinimalReleaseDate_ShouldNotThrowException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28)); // Минимально допустимая дата
        film.setDuration(120L);

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void create_FilmWithNegativeDuration_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-1L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithZeroDuration_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0L);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void create_FilmWithPositiveDuration_ShouldNotThrowException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(1L); // Минимальная положительная продолжительность

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    void update_ValidFilm_ShouldUpdateFilm() {
        // Сначала создаем фильм
        Film film = new Film();
        film.setName("Original Name");
        film.setDescription("Original Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        Film createdFilm = filmController.create(film);

        // Обновляем фильм
        createdFilm.setName("Updated Name");
        Film updatedFilm = filmController.update(createdFilm);

        assertEquals("Updated Name", updatedFilm.getName());
    }

    @Test
    void update_NonExistentFilm_ShouldThrowValidationException() {
        Film film = new Film();
        film.setId(999L);
        film.setName("Film Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);

        assertThrows(ValidationException.class, () -> filmController.update(film));
    }

    @Test
    void create_FilmWithNullBody_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> filmController.create(null));
    }

    @Test
    void update_FilmWithNullBody_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> filmController.update(null));
    }
}