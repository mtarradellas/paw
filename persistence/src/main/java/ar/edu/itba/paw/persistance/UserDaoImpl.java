package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("mail"),
            rs.getString("phone"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
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
    public Optional<User> save(User user){
        return jdbcTemplate.query("INSERT INTO users(id, username) VALUES (?, ?, ?, ?, ?)",
                new Object[] {user.getId(), user.getUsername(), user.getPassword(), user.getMail(), user.getPhone()}, USER_MAPPER)
                .stream().findFirst();
    };
}

