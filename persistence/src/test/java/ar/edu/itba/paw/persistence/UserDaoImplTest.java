package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {

    @Autowired
    private DataSource dataSource;

    private static final String USER_TABLE = "users";

    private static final long ID = 1;
    private static final String USERNAME = "user_test_name";
    private static final String PASSWORD = "user_test_password";
    private static final String MAIL = "user_test_@mail";
    private static final String PHONE = "11111111";

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail"),
            rs.getString("phone"));

    private UserDaoImpl userDaoImpl;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {

        userDaoImpl = new UserDaoImpl(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE);
    }

    private void insertUser(long id, String username, String password, String mail, String phone) {
        final Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("username", username);
        values.put("password", password);
        values.put("mail", mail);
        values.put("phone", phone);
        jdbcInsert.execute(values);
    }

    private void assertUser(User user, long id, String username, String password, String mail, String phone) {
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(mail, user.getMail());
        assertEquals(phone, user.getPhone());
    }

    @Test
    public void testFindByIdNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> opUser = userDaoImpl.findById(100);

        assertFalse(opUser.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        insertUser(ID, USERNAME, PASSWORD, MAIL, PHONE);

        Optional<User> opUser = userDaoImpl.findById(ID);

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), ID, USERNAME, PASSWORD, MAIL, PHONE);
    }

    @Test
    public void testFindByUsernameNotExist() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> opUser = userDaoImpl.findByUsername(USERNAME);

        assertFalse(opUser.isPresent());
    }

    @Test
    public void testFindByUsernameExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        insertUser(ID, USERNAME, PASSWORD, MAIL, PHONE);

        Optional<User> opUser = userDaoImpl.findByUsername(USERNAME);

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), ID, USERNAME, PASSWORD, MAIL, PHONE);
    }

    @Test
    public void testFindByMailNotExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> opUser = userDaoImpl.findByMail(MAIL);

        assertFalse(opUser.isPresent());
    }

    @Test
    public void testFindByMailExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        insertUser(ID, USERNAME, PASSWORD, MAIL, PHONE);

        Optional<User> opUser = userDaoImpl.findByMail(MAIL);

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), ID, USERNAME, PASSWORD, MAIL, PHONE);
    }

    @Test
    public void testUpdatePasswordExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        insertUser(ID, USERNAME, PASSWORD, MAIL, PHONE);

        boolean updated = userDaoImpl.updatePassword("new_psw", ID);

        assertTrue(updated);
    }

    @Test
    public void testUpdatePasswordNotExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        boolean updated = userDaoImpl.updatePassword("new_psw", ID);

        assertFalse(updated);
    }

    @Test
    public void testListNotExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        List<User> userList = userDaoImpl.list().collect(Collectors.toList());

        assertTrue(userList.isEmpty());
    }

    @Test
    public void testListExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        insertUser(ID, USERNAME, PASSWORD, MAIL, PHONE);
        insertUser(2, "other_user", "other_psw", "other_mail", "other_phone");

        List<User> userList = userDaoImpl.list().collect(Collectors.toList());

        assertEquals(2, userList.size());
    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> opUser = userDaoImpl.create(USERNAME, PASSWORD, MAIL, PHONE);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertTrue(opUser.isPresent());
        Optional<User> jdbcOpUser = jdbcTemplate.query("SELECT * FROM users", USER_MAPPER).stream().findFirst();
        assertTrue(jdbcOpUser.isPresent());
        User jdbcUser = jdbcOpUser.get();
        assertUser(opUser.get(), jdbcUser.getId(), jdbcUser.getUsername(),
                jdbcUser.getPassword(), jdbcUser.getMail(), jdbcUser.getPhone());
        assertUser(opUser.get(), jdbcUser.getId(), USERNAME, PASSWORD, MAIL, PHONE);
    }

    /* TODO Test tokens */
    public void testFindByTokenNotExists() {

    }

    public void testFindByTokenExists() {

    }

    public void testGetTokenNotExists() {

    }

    public void testGetTokenExists() {

    }

    public void testDeleteTokenNotExists() {

    }

    public void testDeleteTokenExists() {

    }

    public void testCreateToken() {

    }
}
