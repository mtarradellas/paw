package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name"));

    private Map<String, User> users = new ConcurrentHashMap<String, User>();

    @Autowired
    public UserDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Constructor for testing
    public UserDaoImpl() {

        User user = new User();
        user.setId(1);
        user.setUsername("Leo");
        user.setPassword("1234");
        users.put("1", user);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("Annabeth");
        user2.setPassword("12345");
        users.put("2", user2);

        User user3 = new User();
        user3.setId(3);
        user3.setUsername("Percy");
        users.put("3", user3);
    }

    @Override
    public User findById(long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[] {id}, USER_MAPPER);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE username = '?'", new Object[] {username}, USER_MAPPER);

        return users.isEmpty() ? null : users.get(0);
    }

    public List<User> list() {
        return new ArrayList<User>(this.users.values());
    }

    public User save(User user){
        return this.users.put(user.getUsername(), user);
    };
}

