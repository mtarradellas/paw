package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> list(int page, int pageSize);
    List<User> filteredList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<UserStatus> filteredStatusList(List<String> find, UserStatus status);
    int getListAmount();
    int getFilteredAmount(List<String> find, UserStatus status);

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByToken(UUID token);

    Optional<User> create(String username, String password, String mail, String locale, String contextURL);
    Optional<User> update(User user);
    Optional<User> updateUsername(long id, String username);
    Optional<User> updateStatus(long id, UserStatus status);
    Optional<User> updateLocale(long id, String locale);
    Optional<User> updateLocale(User user, String locale);
    Optional<User> updatePassword(long id, String oldPassword, String newPassword) throws InvalidPasswordException;
    Optional<User> requestPasswordReset(String mail, String contextURL);
    Optional<User> resetPassword(UUID token, String password);

    List<Review> reviewList(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status,
                            String criteria, String order, int page, int pageSize);
    int getReviewListAmount(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status);
    Optional<Review> findReviewById(long id);
    boolean addReview(User owner, long targetId, int score, String description);
    Optional<Review> updateReview(Review review);
    Optional<Review> updateReview(long id, long ownerId, long targetId, int score, String description);
    Optional<Review> updateReview(long id, User owner, long targetId, int score, String description);
    double getReviewAverage(long userId);

    Optional<User> adminCreate(String username, String password, String mail, String locale);
    boolean isAdmin(User user);
    boolean recoverUser(long id);
    boolean removeUser(long id);

    Optional<Token> getToken(UUID token);
    Optional<Token> createToken(UUID token, User user);
    boolean deleteToken(UUID token);
    Optional<User> activateAccountWithToken(UUID token);

}
