package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.MailType;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private PetService petService;
    @Autowired
    private RequestService requestService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> list(int page, int pageSize) {
        return userDao.list(page, pageSize);
    }

    @Override
    public List<User> filteredList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return userDao.searchList(find, status, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public List<UserStatus> filteredStatusList( List<String> find, UserStatus status) {
        Set<Integer> results = userDao.searchStatusList(find, status);
        List<UserStatus> toReturn = new ArrayList<>();
        results.stream().forEach(r->toReturn.add(UserStatus.values()[r]));
        return toReturn;
    }

    @Override
    public int getListAmount() {
        return userDao.getListAmount();
    }

    @Override
    public int getFilteredAmount(List<String> find, UserStatus status) {
        return userDao.getSearchAmount(find, status);
    }

    @Override
    public Optional<User> findById(Long id) {
        if(id == null ) return Optional.empty();
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return userDao.findByMail(mail);
    }

    @Override
    public Optional<User> findByToken(UUID token) {
        return userDao.findByToken(token);
    }


    @Transactional
    @Override
    public Optional<User> create(String username, String password, String mail, String locale, String contextURL) {
        LOGGER.debug("Attempting user creation with username: {}, mail: {}", username, mail);
        User user = userDao.create(username, encoder.encode(password), mail, UserStatus.INACTIVE, locale);

        UUID uuid = UUID.randomUUID();
        if (!createToken(uuid, user).isPresent()) {
            LOGGER.warn("Token for user {} could not be created", user.getId());
            return Optional.empty();
        }

        Map<String, Object> arguments = new HashMap<>();
        String urlToken = contextURL + "/account-activation";
        urlToken += "?token=" + uuid;

        arguments.put("URLToken", urlToken );
        arguments.put("username",user.getUsername());

        String userLocale = user.getLocale();

        mailService.sendMail(user.getMail(), userLocale, arguments, MailType.ACTIVATE_ACCOUNT);

        LOGGER.debug("Successfully created user; id: {} username: {},  mail: {}", user.getId(), user.getUsername(), user.getMail());
        return Optional.of(user);
    }

    @Transactional
    @Override
    public Optional<User> update(User user) {
        return userDao.update(user);
    }

    @Transactional
    @Override
    public Optional<User> updateUsername(long id, String username) {
        LOGGER.debug("Attempting user {} update with username: {}", id, username);

        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", id);
            return Optional.empty();
        }
        User user = opUser.get();

        user.setUsername(username);
        Optional<User> opUpdatedUser = userDao.update(user);
        if (!opUpdatedUser.isPresent()) {
            LOGGER.warn("Failed to update user {} with new name {}", user.getId(), username);
            return Optional.empty();
        }
        User updatedUser = opUpdatedUser.get();
        LOGGER.debug("Successfully updated user; id: {} username: {}", updatedUser.getId(), updatedUser.getUsername());
        return opUpdatedUser;
    }

    @Transactional
    @Override
    public Optional<User> updateStatus(long id, UserStatus status) {
        LOGGER.debug("Attempting user {} update with status: {}", id, status.getValue());

        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", id);
            return Optional.empty();
        }
        User user = opUser.get();

        user.setStatus(status);
        Optional<User> opUpdatedUser = userDao.update(user);
        if (!opUpdatedUser.isPresent()) {
            LOGGER.warn("Failed to update user {} with new status {}", user.getId(), status.getValue());
            return Optional.empty();
        }
        User updatedUser = opUpdatedUser.get();
        LOGGER.debug("Successfully updated user; id: {} status: {}", updatedUser.getId(), updatedUser.getStatus().getValue());
        return opUpdatedUser;
    }

    @Transactional
    @Override
    public Optional<User> updateLocale(long id, String locale) {
        LOGGER.debug("Attempting userID {} update with locale: {}", id, locale);

        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", id);
            return Optional.empty();
        }
        return updateLocale(opUser.get(), locale);
    }

    @Transactional
    @Override
    public Optional<User> updateLocale(User user, String locale) {
        LOGGER.debug("Attempting user {} update with locale: {}", user, locale);

        user.setLocale(locale);
        Optional<User> opUpdatedUser = userDao.update(user);
        if (!opUpdatedUser.isPresent()) {
            LOGGER.warn("Failed to update user{} with new locale {}", user.getId(), locale);
            return Optional.empty();
        }
        User updatedUser = opUpdatedUser.get();
        LOGGER.debug("Successfully updated user; id: {} locale: {}", updatedUser.getId(), updatedUser.getLocale());
        return opUpdatedUser;
    }

    @Transactional
    @Override
    public Optional<User> updatePassword(long id, String oldPassword, String newPassword) throws InvalidPasswordException {
        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) {
            LOGGER.warn("User {} not found", id);
            return Optional.empty();
        }
        User user = opUser.get();

        if(oldPassword != null){
            LOGGER.debug("Checking old password");
            if(! encoder.matches(oldPassword, user.getPassword())){
                LOGGER.warn("Password does not match the current one");
                throw new InvalidPasswordException("Password does not match the current one");
            }
        }
        LOGGER.debug("Valid old password");
        user.setPassword(encoder.encode(newPassword));
        Optional<User> opUpdatedUser = userDao.update(user);
        if(!opUpdatedUser.isPresent()){
            LOGGER.warn("DAO could not update password");
            return Optional.empty();
        }
        LOGGER.debug("Password updated");
        return opUpdatedUser;
    }

    @Transactional
    @Override
    public Optional<User> requestPasswordReset(String mail, String contextURL) {
        LOGGER.debug("Requesting password reset for mail {}", mail);

        Optional<User> opUser = userDao.findByMail(mail);
        if (!opUser.isPresent()) {
            LOGGER.debug("User with mail {} not found", mail);
            return Optional.empty();
        }
        final User user = opUser.get();

        UUID token = UUID.randomUUID();
        createToken(token, user);

        Map<String, Object> arguments = new HashMap<>();

        String urlToken = contextURL + "/password-reset";
        urlToken += "?token=" + token;

        arguments.put("URLToken", urlToken );
        arguments.put("username",user.getUsername());

        String userLocale = user.getLocale();

        mailService.sendMail(user.getMail(), userLocale, arguments, MailType.RESET_PASSWORD);
        return opUser;
    }

    @Transactional
    @Override
    public Optional<User> resetPassword(UUID uuid, String password) {
        LOGGER.debug("Resetting password for token {}", uuid);

        Optional<Token> opToken = getToken(uuid);
        if (!opToken.isPresent()) {
            LOGGER.warn("Token {} not found", uuid);
            return Optional.empty();
        }
        final Token token = opToken.get();

        if (LocalDateTime.now().isAfter(token.getExpirationDate())) {
            LOGGER.warn("Token {} has expired", uuid);
            userDao.deleteToken(uuid);
            return Optional.empty();
        }

        Optional<User> opUser = findByToken(uuid);
        if (!opUser.isPresent()) {
            LOGGER.warn("User of token {} not found", uuid);
            userDao.deleteToken(uuid);
            return Optional.empty();
        }
        final User user = opUser.get();
        Optional<User> updatedUser = Optional.empty();

        updatedUser = updatePassword(user.getId(), null, password);

        deleteToken(uuid);

        return updatedUser;
    }

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
    public boolean addReview(User owner, long targetId, int score, String description) {
        Optional<User> opTarget = userDao.findById(targetId);
        if (!opTarget.isPresent()) {
            LOGGER.warn("Review target {} was not found", targetId);
            return false;
        }
        User target = opTarget.get();

        if (owner.getId().equals(target.getId())) {
            LOGGER.warn("Target of review is the same as the owner, ignoring review {}", owner.getId());
            return false;
        }
        userDao.addReview(owner, target, score, description, ReviewStatus.VALID);
        return true;
    }

    @Override
    public Optional<Review> updateReview(Review review) {
        return userDao.updateReview(review);
    }

    @Override
    public Optional<Review> updateReview(long id, long ownerId, long targetId, int score, String description) {
        Optional<User> owner = userDao.findById(ownerId);
        if (!owner.isPresent()) {
            LOGGER.warn("Owner {} of review not found", ownerId);
            return Optional.empty();
        }
        return updateReview(id, owner.get(), targetId, score, description);
    }

    @Override
    public Optional<Review> updateReview(long id, User owner, long targetId, int score, String description) {
        Optional<Review> opReview = userDao.findReviewById(id);
        if (!opReview.isPresent()) {
            LOGGER.warn("Review {} not found", id);
            return Optional.empty();
        }
        Review review = opReview.get();

        Optional<User> opTarget = userDao.findById(targetId);
        if (!opTarget.isPresent()) {
            LOGGER.warn("Target {} of review not found", targetId);
            return Optional.empty();
        }
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

    @Transactional
    @Override
    public Optional<User> adminCreate(String username, String password, String mail, String locale) {
        LOGGER.debug("(Admin) Attempting user creation with username: {}, mail: {}", username, mail);
        User user = userDao.create(username, encoder.encode(password), mail, UserStatus.ACTIVE, locale);
        return Optional.of(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return userDao.isAdmin(user);
    }

    @Transactional
    @Override
    public boolean recoverUser(long id) {
        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) return false;
        User user = opUser.get();
        return updateStatus(user.getId(), UserStatus.ACTIVE).isPresent();
    }

    @Transactional
    @Override
    public boolean removeUser(long id) {
        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) return false;
        User user = opUser.get();
        requestService.rejectAllByPetOwner(user.getId()); //cancels all (pending) requests made to pets this user owns
        requestService.cancelAllByUser(user);
        petService.removeAllByUser(user);
        return updateStatus(user.getId(), UserStatus.DELETED).isPresent();
    }

    @Override
    public Optional<Token> getToken(UUID token) {
        return userDao.findToken(token);
    }

    @Transactional
    @Override
    public Optional<Token> createToken(UUID token, User user) {
        return userDao.createToken(token, user, LocalDateTime.now().plusDays(1));
    }

    @Transactional
    @Override
    public boolean deleteToken(UUID token) {
        return userDao.deleteToken(token);
    }

    @Transactional
    @Override
    public Optional<User> activateAccountWithToken(UUID uuid) {
        LOGGER.debug("Activating account token {}", uuid);

        Optional<Token> opToken = getToken(uuid);
        if (!opToken.isPresent()) {
            LOGGER.warn("Token {} not found", uuid);
            return Optional.empty();
        }
        final Token token = opToken.get();
        if (LocalDateTime.now().isAfter(token.getExpirationDate())) {
            LOGGER.warn("Token {} has expired", uuid);
            userDao.deleteToken(uuid);
            return Optional.empty();
        }

        Optional<User> opUser = findByToken(uuid);
        if (!opUser.isPresent()) {
            LOGGER.warn("User of token {} not found", uuid);
            userDao.deleteToken(uuid);
            return Optional.empty();
        }
        User user = opUser.get();

        if(!updateStatus(user.getId(), UserStatus.ACTIVE).isPresent()) {
            LOGGER.warn("Could not activate user {} account", user.getId());
            userDao.deleteToken(uuid);
            return Optional.empty();
        }

        deleteToken(uuid);
        return opUser;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void cleanOldTokens() {
        userDao.cleanOldTokens();
    }
}