package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.ReviewStatus;

public interface ReviewService {
    List<Review> reviewList(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status,
                            String criteria, String order, int page, int pageSize);
    int getReviewListAmount(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status);

    Optional<Review> findReviewById(long id);
    Optional<Review> addReview(long userId, long targetId, int score, String description);
    Optional<Review> updateReview(Review review);
    Optional<Review> updateReview(long id, long ownerId, long targetId, int score, String description);
    Optional<Review> updateReview(long id, User owner, long targetId, int score, String description);
    Optional<Review> updateReviewScore(long id, int score);
    Optional<Review> updateReviewScore(long userId, long id, int score);
    Optional<Review> updateReviewDescription(long id, String description);
    Optional<Review> updateReviewDescription(long userId, long id, String description);
    
    void removeReview(long id);
    void removeReview(long userId, long id);
    void recoverReview(long id);
    double getReviewAverage(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status);
}
