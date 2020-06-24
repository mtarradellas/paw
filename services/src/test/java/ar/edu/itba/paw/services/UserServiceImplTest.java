package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String LOCALE = "en_US";
    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 50;

    /* USER */
    private static final Long USER_ID = 1L;
    private static User USER = new User("username", "password", "mail@mail.com", UserStatus.ACTIVE, LOCALE);

    private static final Long O_USER_ID = 2L;
    private static User O_USER = new User("other_username", "other_password", "other_mail@mail.com", UserStatus.ACTIVE, LOCALE);

    /* REVIEW */
    private static final Long REVIEW_ID =1L;
    private static Review REVIEW = new Review(USER, O_USER, 5, "description", ReviewStatus.VALID, LocalDateTime.now());


    @InjectMocks
    private final UserServiceImpl userServiceImpl = new UserServiceImpl();

    @Mock
    private UserDao userDao;

    @Before
    public void setUp() {
        USER.setId(USER_ID);
        O_USER.setId(O_USER_ID);
        REVIEW.setId(REVIEW_ID);
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
        assertEquals(review.getOwner().getId().longValue(), owner);
        assertEquals(review.getTarget().getId().longValue(), target);
        assertEquals(review.getScore(), score);
        assertEquals(review.getDescription(), description);
        assertEquals(review.getStatus(), status);
    }

    @Test
    public void testFilteredListStatus() {
        List<User> userList = new ArrayList<>();
        userList.add(USER);
        when(userDao.searchList(any(), eq(USER.getStatus()), any(), any(), anyInt(), anyInt())).thenReturn(userList);

        List<User> returnList = userServiceImpl.filteredList(null, USER.getStatus(), null, null, PAGE, PAGE_SIZE);

        assertEquals(1, returnList.size());
        assertUser(returnList.get(0), USER.getId(), USER.getUsername(), USER.getPassword(), USER.getMail(), USER.getStatus(), USER.getLocale());

    }

    @Test
    public void testFindById() {
        when(userDao.findById(eq(USER.getId()))).thenReturn(Optional.of(USER));

        Optional<User> opUser = userServiceImpl.findById(USER.getId());

        assertTrue(opUser.isPresent());
        assertUser(opUser.get(), USER.getId(), USER.getUsername(), USER.getPassword(),USER.getMail(),USER.getStatus(),USER.getLocale());
    }

    @Test
    public void testListReviews() {
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(REVIEW);
        when(userDao.reviewList(eq(REVIEW.getOwner().getId()),eq(REVIEW.getTarget().getId()), anyInt(), anyInt(), eq(REVIEW.getStatus()),
                any(), any(), anyInt(), anyInt())).thenReturn(reviewList);

        List<Review> returnList = userServiceImpl.reviewList(REVIEW.getOwner().getId(), REVIEW.getTarget().getId(), 0,
                5, REVIEW.getStatus(), null, null, PAGE, PAGE_SIZE);

        assertEquals(1, returnList.size());
        Review review = returnList.get(0);
        assertEquals(REVIEW.getId(), review.getId());
        User owner = review.getOwner();
        User target = review.getTarget();
        assertUser(owner, USER.getId(), USER.getUsername(), USER.getPassword(), USER.getMail(), USER.getStatus(), USER.getLocale());
        assertUser(target, O_USER.getId(), O_USER.getUsername(), O_USER.getPassword(), O_USER.getMail(), O_USER.getStatus(), O_USER.getLocale());
        assertReview(review, REVIEW.getOwner().getId(), REVIEW.getTarget().getId(), REVIEW.getScore(), REVIEW.getDescription(), REVIEW.getStatus());
    }
}
