package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserDaoImplTest {

    private static final String USER_TABLE = "users";
    private static final String USERNAME = "user_test_name";
    private static final String MAIL = "user_test_mail";
    private static final String PHONE = "user_test_phone";


    private UserDaoImpl userDaoImpl;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(JDBCDriver.class);
        dataSource.setUrl("jdbc:hsqldb:mem:paw");
        dataSource.setUsername("ha");
        dataSource.setPassword("");

        userDaoImpl = new UserDaoImpl(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                                + "id INTEGER IDENTITY PRIMARY KEY, "
                                + "username VARCHAR(255), "
                                + "mail VARCHAR(255) NOT NULL, "
                                + "phone VARCHAR(255)"
                                + ");");
    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User user = userDaoImpl.create(USERNAME, MAIL, PHONE);

        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
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
}
