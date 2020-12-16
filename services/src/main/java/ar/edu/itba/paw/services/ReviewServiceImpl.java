package ar.edu.itba.paw.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.ReviewService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.ReviewException;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.ReviewStatus;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private UserDao userDao;
    
    @Override
    public List<Review> reviewList(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status,
                                   String criteria, String order, int page, int pageSize) {
        return userDao.reviewList(ownerId, targetId, minScore, maxScore, status,  criteria, order, page, pageSize);
    }

    @Override
    public int getReviewListAmount(Long ownerId, Long targetId, int minScore, int maxScore, ReviewStatus status) {
        return userDao.getReviewListAmount(ownerId, targetId, minScore, maxScore, status);
    }

    @Override
    public Optional<Review> findReviewById(long id) {
        return userDao.findReviewById(id);
    }

    @Transactional
    @Override
    public Optional<Review> addReview(long userId, long targetId, int score, String description) {
        Optional<User> opTarget = userDao.findById(targetId);
        if (!opTarget.isPresent()) throw new NotFoundException("Target " + targetId + " not found.");
        User target = opTarget.get();

        Optional<User> opUser = userDao.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        if (user.getId().equals(target.getId())) {
            LOGGER.warn("Target of review is the same as the owner ({}), ignoring review.", user.getId());
            throw new ReviewException("Target and Owner of review are the same.");
        }

        if (!userDao.canReview(user, target)) {
            LOGGER.warn("User cannot review. They must adopt a pet first.");
            throw new ReviewException("User cannot review. They must adopt a pet first");
        }

        Review review = userDao.addReview(user, target, score, description, ReviewStatus.VALID);
        return Optional.of(review);
    }

    @Override
    public boolean canReview(long userId, long targetId) {
        Optional<User> opTarget = userDao.findById(targetId);
        if (!opTarget.isPresent()) throw new NotFoundException("Target " + targetId + " not found.");
        User target = opTarget.get();

        Optional<User> opUser = userDao.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        User user = opUser.get();

        return userDao.canReview(user, target);
    }

    @Override
    public Optional<Review> updateReview(Review review) {
        return userDao.updateReview(review);
    }

    @Override
    public Optional<Review> updateReview(long id, long ownerId, long targetId, int score, String description) {
        Optional<User> owner = userDao.findById(ownerId);
        if (!owner.isPresent()) throw new NotFoundException("User " + id + " not found.");
        return updateReview(id, owner.get(), targetId, score, description);
    }

    @Override
    public Optional<Review> updateReview(long id, User owner, long targetId, int score, String description) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found.");
        Review review = opReview.get();

        Optional<User> opTarget = userDao.findById(targetId);
        if (!opTarget.isPresent()) throw new NotFoundException("Target " + targetId + " not found.");
        User target = opTarget.get();

        if (owner.getId().equals(target.getId())) {
            LOGGER.warn("User {} cannot review itself!", targetId);
            return Optional.empty();
        }

        review.setOwner(owner);
        review.setTarget(target);
        review.setScore(score);
        review.setDescription(description);

        return userDao.updateReview(review);
    }

    @Override
    @Transactional
    public Optional<Review> updateReviewScore(long id, int score) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found");
        Review review = opReview.get();

        review.setScore(score);
        return userDao.updateReview(review);
    }

    @Override
    @Transactional
    public Optional<Review> updateReviewScore(long userId, long id, int score) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found");
        Review review = opReview.get();

        Optional<User> opUser = userDao.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found");
        User user = opUser.get();

        if (!review.getOwner().getId().equals(user.getId())) {
            throw new ReviewException("User is not the owner of the review.");
        }

        review.setScore(score);
        return userDao.updateReview(review);
    }

    @Override
    @Transactional
    public Optional<Review> updateReviewDescription(long id, String description) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found");
        Review review = opReview.get();

        review.setDescription(description);
        return userDao.updateReview(review);
    }

    @Override
    @Transactional
    public Optional<Review> updateReviewDescription(long userId, long id, String description) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found");
        Review review = opReview.get();

        Optional<User> opUser = userDao.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found");
        User user = opUser.get();

        if (!review.getOwner().getId().equals(user.getId())) {
            throw new ReviewException("User is not the owner of the review.");
        }

        review.setDescription(description);
        return userDao.updateReview(review);
    }

    @Override
    @Transactional
    public void removeReview(long id) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found.");
        Review review = opReview.get();
        review.setStatus(ReviewStatus.REMOVED);
        userDao.updateReview(review);
    }

    @Override
    @Transactional
    public void removeReview(long userId, long id) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found.");
        Optional<User> opUser = userDao.findById(userId);
        if (!opUser.isPresent()) throw new NotFoundException("User " + userId + " not found.");
        
        Review review = opReview.get();
        User user = opUser.get();
        if (!review.getOwner().getId().equals(user.getId())) {
            throw new ReviewException("User is not the owner of the review.");
        }

        review.setStatus(ReviewStatus.REMOVED);
        userDao.updateReview(review);
    }

    @Override
    @Transactional
    public void recoverReview(long id) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) throw new NotFoundException("Review " + id + " not found.");
        Review review = opReview.get();
        review.setStatus(ReviewStatus.VALID);
        userDao.updateReview(review);
    }

    @Override
    public double getReviewAverage(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status) {
        return userDao.getReviewAverage(userId, targetId, minScore, maxScore, status);
    }
}
