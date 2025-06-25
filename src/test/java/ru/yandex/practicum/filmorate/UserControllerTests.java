package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTests {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void create_ValidUser_ShouldReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        User createdUser = userController.create(user);

        assertNotNull(createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getLogin(), createdUser.getLogin());
    }

    @Test
    void create_UserWithEmptyEmail_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_UserWithInvalidEmailFormat_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_UserWithEmptyLogin_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_UserWithLoginContainingSpaces_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login with spaces");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void create_UserWithNullName_ShouldUseLoginAsName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName(null);
        user.setBirthday(LocalDate.now().minusYears(20));

        User createdUser = userController.create(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void create_UserWithEmptyName_ShouldUseLoginAsName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("");
        user.setBirthday(LocalDate.now().minusYears(20));

        User createdUser = userController.create(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void create_UserWithBlankName_ShouldUseLoginAsName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("   ");
        user.setBirthday(LocalDate.now().minusYears(20));

        User createdUser = userController.create(user);

        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void create_UserWithFutureBirthday_ShouldThrowValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void update_ValidUser_ShouldUpdateUser() {
        // Сначала создаем пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().minusYears(20));
        User createdUser = userController.create(user);

        // Обновляем пользователя
        createdUser.setEmail("updated@example.com");
        User updatedUser = userController.update(createdUser);

        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void update_NonExistentUser_ShouldThrowValidationException() {
        User user = new User();
        user.setId(999L);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ValidationException.class, () -> userController.update(user));
    }

    @Test
    void create_UserWithNullBody_ShouldThrowValidationException() {
        assertThrows(NullPointerException.class, () -> userController.create(null));
    }

    @Test
    void update_UserWithNullBody_ShouldThrowValidationException() {
        assertThrows(NullPointerException.class, () -> userController.update(null));
    }
}