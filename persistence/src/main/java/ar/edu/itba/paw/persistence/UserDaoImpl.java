package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String USER_TABLE = "users";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail"),
            rs.getString("phone"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
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
    public Stream<User> list() {
        return jdbcTemplate.query("SELECT * FROM users", USER_MAPPER)
                .stream();
    }

    @Override
    public User create(String username, String password, String mail, String phone){
        final Map<String, String> values = new HashMap<>();
        values.put("username", username);
        values.put("password", password);
        values.put("mail", mail);
        values.put("phone", phone);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return new User(key.longValue(), username, password, mail, phone);
    };
}

