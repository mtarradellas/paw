package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {

    private static final String USER_TABLE = "users";
    private static final String USERNAME = "user_test_name";
    private static final String PASSWORD = "user_test_password";
    private static final String MAIL = "user_test_mail";
    private static final String PHONE = "user_test_phone";

    @Autowired
    private DataSource dataSource;

    private UserDaoImpl userDaoImpl;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {

        userDaoImpl = new UserDaoImpl(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User user = userDaoImpl.create(USERNAME, PASSWORD, MAIL, PHONE);

        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(MAIL, user.getMail());
        assertEquals(PHONE, user.getPhone());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFindByIdDoesNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> maybeUser = userDaoImpl.findById(100);

        assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        final Map<String, String> values = new HashMap<>();
        values.put("username", USERNAME);
        values.put("password", PASSWORD);
        values.put("mail", MAIL);
        values.put("phone", PHONE);
        final Number key = jdbcInsert.executeAndReturnKey(values);

        Optional<User> maybeUser = userDaoImpl.findById(key.longValue());

        assertTrue(maybeUser.isPresent());
        User user = maybeUser.get();
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(MAIL, user.getMail());
        assertEquals(PHONE, user.getPhone());
    }
}
