package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("username"));

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[] {id}, USER_MAPPER);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE username = ?", new Object[] {username}, USER_MAPPER);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> list() {
        return jdbcTemplate.query("SELECT * FROM users", USER_MAPPER);
    }

    @Override
    public User save(User user){
        List<User> users = jdbcTemplate.query("INSERT INTO users(id, username) VALUES (?, ?)", new Object[] {user.getId(), user.getUsername()}, USER_MAPPER);

        return users.isEmpty() ? null : users.get((0));
    };
}

