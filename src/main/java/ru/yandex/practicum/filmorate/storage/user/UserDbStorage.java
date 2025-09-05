package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NoContentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository implements UserStorage {
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users;";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?;";
    private static final String INSERT_USER_QUERY = "INSERT INTO users(login, email, name, birthday) " +
            "VALUES(?,?,?,?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET login = ?, email = ?, name = ?, " +
            "birthday = ? WHERE user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendships(user_id,friend_id) " +
            "VALUES(?,?)";
    private static final String GET_USER_FRIENDS_IDS_QUERY = "SELECT friend_id FROM friendships WHERE user_id = ?;";
    private static final String REMOVE_USER_FRIEND_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findUsers() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        Optional<User> optionalUser = findOne(FIND_USER_BY_ID_QUERY, id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFriends(user.getFriends());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> createUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный вид email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        Long id = insert(INSERT_USER_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (findUserById(user.getId()).isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        update(UPDATE_USER_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return findUserById(user.getId());
    }

    @Override
    public Optional<User> addFriend(Long userId, Long friendId) {
        if (findUserById(friendId).isEmpty()) {
            throw new NotFoundException("Невозможно добавить в друзья пользователя, т.к. его не существует");
        }
        update(ADD_FRIEND_QUERY, userId, friendId);
        User user = findUserById(userId).get();
        user.addFriend(friendId);
        return Optional.of(user);
    }

    @Override
    public List<User> getFriends(Long userId) {
        if (findUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        List<User> userFriends = new ArrayList<>();
        List<Long> friendsIds = jdbc.queryForList(GET_USER_FRIENDS_IDS_QUERY, Long.class, userId);
        for (Long friendId : friendsIds) {
            userFriends.add(findUserById(friendId).get());
        }
        return userFriends;
    }

    @Override
    public Optional<User> alienateFriends(Long userId, Long friendId) {
        if (findUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        if (findUserById(friendId).isEmpty()) {
            throw new NotFoundException("Друга с таким id не существует");
        }
        if (!getFriends(userId).contains(findUserById(friendId).get())) {
            throw new NoContentException("Пользователь не является другом");
        }
        update(REMOVE_USER_FRIEND_QUERY, userId, friendId);
        return findUserById(userId);
    }

    @Override
    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        List<Long> user1FriendsIds = jdbc.queryForList(GET_USER_FRIENDS_IDS_QUERY, Long.class, user1Id);
        List<Long> user2FriendsIds = jdbc.queryForList(GET_USER_FRIENDS_IDS_QUERY, Long.class, user2Id);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : user1FriendsIds) {
            if (user2FriendsIds.contains(id)) {
                commonFriends.add(findUserById(id).get());
            }
        }
        return commonFriends;
    }
}
