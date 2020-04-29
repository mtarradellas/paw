package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String USER_TABLE = "users";
    private static final String TOKEN_TABLE = "tokens";

    private static final String DUPLICATE_USERNAME_ERROR = "Duplicate key exception: username already exists";
    private static final String DUPLICATE_MAIL_ERROR = "Duplicate key exception: mail already exists";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertToken;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail"),
            rs.getString("phone"));

    private static final RowMapper<Token> TOKEN_MAPPER = (rs, rowNum) -> new Token(
            rs.getLong("id"),
            (UUID)rs.getObject("token"),
            rs.getLong("userId"),
            rs.getDate("expirationDate"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertToken = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TOKEN_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[] {id}, USER_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", new Object[] {username}, USER_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", new Object[] {mail}, USER_MAPPER)
                .stream().findFirst();
    }

    @Override
    public boolean updatePassword(String newPassword, long id) {
        return jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", new Object[] {newPassword, id}) == 1;
    }

    @Override
    public boolean createToken(UUID uuid, long userId, Date expirationDate) {
        final Map<String, Object> values = new HashMap<>();
        values.put("token", uuid);
        values.put("userId", userId);
        values.put("expirationDate", expirationDate);
        return jdbcInsertToken.executeAndReturnKey(values).intValue() > 0 ;

    }
    @Override
    public Optional<Token> getToken(UUID uuid) {
        return jdbcTemplate.query("SELECT * FROM tokens WHERE token = ? ", new Object[] {uuid},TOKEN_MAPPER)
                .stream().findFirst();
    }

    @Override
    public boolean deleteToken(UUID uuid) {
        return jdbcTemplate.update("DELETE FROM tokens WHERE token = ?", new Object[] {uuid}) == 1;
    }

    @Override
    public Optional<User> findByToken(UUID uuid) {
        return jdbcTemplate.query("SELECT users.id AS id, username, password, mail, phone " +
                "FROM users INNER JOIN tokens ON users.id = tokens.userid " +
                "WHERE tokens.token = ? ", new Object[] {uuid}, USER_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Stream<User> list() {
        return jdbcTemplate.query("SELECT * FROM users", USER_MAPPER)
                .stream();
    }

    @Override
    public Optional<User> create(String username, String password, String mail, String phone) throws DuplicateUserException {
        final Map<String, String> values = new HashMap<>();
        values.put("username", username);
        values.put("password", password);
        values.put("mail", mail);
        values.put("phone", phone);
        Number key;

        try {
            key = jdbcInsert.executeAndReturnKey(values);
        } catch (DuplicateKeyException ex) {
            System.out.println(ex.getMessage());
            if (ex.getMessage().contains("users_username_key")) throw new DuplicateUserException(DUPLICATE_USERNAME_ERROR, true, false);
            if (ex.getMessage().contains("users_mail_key")) throw new DuplicateUserException(DUPLICATE_MAIL_ERROR, false, true);
            return Optional.empty();
        }

        return Optional.of(new User(key.longValue(), username, password, mail, phone));
    }
}

