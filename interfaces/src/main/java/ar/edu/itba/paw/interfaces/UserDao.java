package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserDao {

    List<User> list(int page, int pageSize);
    List<User> searchList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<User> filteredList(UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    Set<Integer> searchStatusList(List<String> find, UserStatus status);
    int getListAmount();
    int getSearchAmount(List<String> find, UserStatus status);
    int getFilteredAmount(UserStatus status);

    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByToken(UUID token);

    User create(String username, String password, String mail, UserStatus status, String locale);
    Optional<User> update(User user);
    boolean isAdmin(User user);

    List<Review> reviewList(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status,
                            String criteria, String order, int page, int pageSize);
    int getReviewListAmount(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status);
    Optional<Review> findReviewById(long id);
    Review addReview(User owner, User target, int score, String description, ReviewStatus status);
    Optional<Review> updateReview(Review review);
    double getReviewAverage(long userId);

    List<Token> listTokens();
    Optional<Token> findToken(UUID token);
    Optional<Token> createToken(UUID token, User user, LocalDateTime expirationDate);
    boolean deleteToken(UUID token);
    void cleanOldTokens();

    void indexUsers();
}
