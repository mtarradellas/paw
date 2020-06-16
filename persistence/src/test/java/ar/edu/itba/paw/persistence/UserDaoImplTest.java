package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.util.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {

    @Autowired
    private DataSource dataSource;

    private static final String USER_TABLE = "users";

    /* USER */
    private static Long USER_ID;
    private static final String USERNAME = "user_name";
    private static final String PASSWORD = "user_password";
    private static final String MAIL = "user@mail";
    private static final UserStatus USER_STATUS = UserStatus.ACTIVE;
    private static final String LOCALE = "en_US";

    /* OTHER USER */
    private static Long O_USER_ID;
    private static final String O_USERNAME = "other_name";
    private static final String O_PASSWORD = "other_password";
    private static final String O_MAIL = "other@mail";
    private static final UserStatus O_USER_STATUS = UserStatus.ACTIVE;
    private static final String O_LOCALE = "en_US";

    /* REVIEW */
    private static Long REVIEW_ID;
    private static final int SCORE = 5;
    private static final String DESC = "GREATEST_USER_OF_ALL_TIME";
    private static final ReviewStatus REVIEW_STATUS = ReviewStatus.VALID;

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;

    private UserJpaDaoImpl userDaoImpl;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {

        userDaoImpl = new UserJpaDaoImpl();

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
    }

    private User insertUser(String username, String password, String mail, UserStatus status, String locale) {
        final Map<String, Object> values = new HashMap<>();
        values.put("username", username);
        values.put("password", password);
        values.put("mail", mail);
        values.put("status", status.ordinal());
        values.put("locale", locale);
        Number id = jdbcInsert.executeAndReturnKey(values);
        User user = new User(username, password, mail, status, locale);
        user.setId(id.longValue());
        return user;
    }

    private void assertUser(User user, Long id, String username, String password, String mail, UserStatus status, String locale) {
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(mail, user.getMail());
        assertEquals(status, user.getStatus());
        assertEquals(locale, user.getLocale());
    }

    private void assertReview(Review review, long owner, long target, int score, String description, ReviewStatus status) {
        assertEquals(review.getId().longValue(), owner);
        assertEquals(review.getId().longValue(), target);
        assertEquals(review.getScore(), score);
        assertEquals(review.getDescription(), description);
        assertEquals(review.getStatus(), status);
    }

    @Transactional
    @Test
    public void testFindByIdNotExist() {
        Optional<User> opUser = userDaoImpl.findById(100);

        assertFalse(opUser.isPresent());
    }

    @Test
    @Transactional
    public void testFindByIdExists() {
        User user = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);

        System.out.println("Searching for user: " + user);
        Optional<User> opUser = userDaoImpl.findById(user.getId());

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }
/*
    @Test
    public void testFindByUsernameNotExist() {
        Optional<User> opUser = userDaoImpl.findByUsername(USERNAME);

        assertFalse(opUser.isPresent());
    }

    @Test
    public void testFindByUsernameExists() {
        User user = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);

        Optional<User> opUser = userDaoImpl.findByUsername(USERNAME);

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }

    @Test
    public void testFindByMailNotExists() {
        Optional<User> opUser = userDaoImpl.findByMail(MAIL);

        assertFalse(opUser.isPresent());
    }

    @Test
    public void testFindByMailExists() {
        User user = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);

        Optional<User> opUser = userDaoImpl.findByMail(MAIL);

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }

    @Test
    public void testSearchList() {
        User user = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
        insertUser(O_USERNAME, O_PASSWORD, O_MAIL, O_USER_STATUS, O_LOCALE);
        List<String> find = new ArrayList<>();
        find.add(USERNAME);

        List<User> userList = userDaoImpl.searchList(find, USER_STATUS, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, userList.size());
        assertUser(userList.get(0), user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }

    @Test
    public void testFilteredList() {
        User user = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
        insertUser(O_USERNAME, O_PASSWORD, O_MAIL, UserStatus.INACTIVE, O_LOCALE);

        List<User> userList = userDaoImpl.filteredList(USER_STATUS, null, null, PAGE, PAGE_SIZE);

        assertEquals(1, userList.size());
        assertUser(userList.get(0), user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }

    @Test
    public void testCreateUser() {
        User user = userDaoImpl.create(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);

        assertNotNull(user);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertUser(user, user.getId(), USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
    }

    @Test
    public void testAddReview() {
        User owner = insertUser(USERNAME, PASSWORD, MAIL, USER_STATUS, LOCALE);
        User target = insertUser(O_USERNAME, O_PASSWORD, O_MAIL, UserStatus.INACTIVE, O_LOCALE);

        userDaoImpl.addReview(owner, target, SCORE, DESC, REVIEW_STATUS);

        assertEquals(1, owner.getOwnerReviews().size());
        assertEquals(1, target.getTargetReviews().size());
        assertEquals(0, owner.getTargetReviews().size());
        assertEquals(0, target.getOwnerReviews().size());
        assertReview(owner.getOwnerReviews().get(0), owner.getId(), target.getId(), SCORE, DESC, REVIEW_STATUS);
    }
*/
//    /* TODO Test tokens */
//    public void testFindByTokenNotExists() {
//
//
//    }
//
//    public void testGetTokenExists() {
//
//    }
//
//    public void testDeleteTokenNotExists() {
//
//    }
//
//    public void testDeleteTokenExists() {
//
//    }
//
//    public void testCreateToken() {
//
//    }
}
