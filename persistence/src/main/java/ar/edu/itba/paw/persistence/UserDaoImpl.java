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

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("mail"),
            rs.getString("phone"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
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
    public Optional<User> create(User user){
        final Map<String, Object> values = new HashMap<String, Object>() {{
            put("username", user.getUsername());
            put("mail", user.getMail());
            put("phone", user.getPhone());
        }};
        final Number key = jdbcInsert.executeAndReturnKey(values);
        user.setId(key.longValue());
        return Optional.of(user);
    };
}

