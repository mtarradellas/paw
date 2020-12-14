package ar.edu.itba.paw.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.InvalidPasswordException;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.MailType;
import ar.edu.itba.paw.models.constants.UserStatus;

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
        results.forEach(r->toReturn.add(UserStatus.values()[r]));
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
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
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
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
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
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
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
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
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
        if (!opUser.isPresent()) throw new NotFoundException("User with mail " + mail + " not found.");
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
        if (!opToken.isPresent()) throw new NotFoundException("Token " + uuid + " not found.");
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
    public String getMail(User user, long userId) {
        final Optional<User> opTarget = userDao.findById(userId);
        if (!opTarget.isPresent()) throw new NotFoundException("User not found.");
        final User target = opTarget.get();

        if (user.getId() == userId || requestService.hasRequest(user, target)) {
            return target.getMail();
        }

        throw new UserException("User has no permissions to view mail.");
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

    @Override
    public boolean isAdminUsername(String username) {
        User user = userDao.findByUsername(username).orElseThrow(NotFoundException::new);
        return userDao.isAdmin(user);
    }

    @Transactional
    @Override
    public boolean recoverUser(long id) {
        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
        User user = opUser.get();
        return updateStatus(user.getId(), UserStatus.ACTIVE).isPresent();
    }

    @Transactional
    @Override
    public boolean removeUser(long id) {
        Optional<User> opUser = userDao.findById(id);
        if (!opUser.isPresent()) throw new NotFoundException("User " + id + " not found.");
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
        if (!opToken.isPresent()) throw new NotFoundException("Token " + uuid + " not found.");
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

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void cleanOldTokens() {
        userDao.cleanOldTokens();
    }
}