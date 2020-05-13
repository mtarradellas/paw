package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Optional<User> findById(String language, long id) {
        return this.userDao.findById(language, id);
    }

    @Override
    public Optional<User> findByUsername(String language, String username) {
        return this.userDao.findByUsername(language, username);
    }

    @Override
    public Stream<User> list(String language) {
        return this.userDao.list(language);
    }

    @Override
    public UserList adminUserList(String locale, String findValue, String status, String searchCriteria, String searchOrder, String page){
        if (findValue == null) return adminFilteredList(locale, status, searchCriteria, searchOrder, page);
        return adminFind(locale, findValue, page);
    }

    @Override
    public UserList adminFind(String language, String findValue, String page) {
        List<User> list = userDao.adminSearchList(language, findValue, page).collect(Collectors.toList());
        String maxPage = getAdminMaxSearchPages(language, findValue);
        return new UserList(list, maxPage);
    }

    @Override
    public UserList adminFilteredList(String language, String status, String searchCriteria, String searchOrder, String page) {
        List<User> list = userDao.adminFilteredList(language, status, searchCriteria, searchOrder, page).collect(Collectors.toList());
        String maxPage = getAdminMaxFilterPages(language, status);
        return new UserList(list, maxPage);
    }

    @Transactional
    @Override
    public Optional<User> create(String language, String username, String password, String mail) throws DuplicateUserException {
        LOGGER.debug("Attempting user creation with username: {}, mail: {}", username, mail);
        Optional<User> opUser = userDao.create(language, username, encoder.encode(password), mail, UserStatus.INACTIVE.getValue());
        if (!opUser.isPresent()) {
            LOGGER.warn("User DAO returned empty user");
            return opUser;
        }

        User user = opUser.get();

        UUID uuid = UUID.randomUUID();
        createToken(uuid, opUser.get().getId());

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7/";
        String urlToken = "http://pawserver.it.itba.edu.ar/paw-2020a-7/account-activation";
        urlToken += "?token=" + uuid;

        arguments.put("URLToken", urlToken );
        arguments.put("URL", url );
        arguments.put("username",user.getUsername());

        mailService.sendMail(user.getMail(), arguments, "activate_account");

        LOGGER.debug("Successfully created user; id: {} username: {},  mail: {}", user.getId(), user.getUsername(), user.getMail());
        return opUser;
    }

    @Override
    public Optional<User> adminCreate(String language, String username, String password, String mail) throws DuplicateUserException {
        LOGGER.debug("Attempting user creation with username: {}, mail: {}", username, mail);
        Optional<User> opUser = userDao.create(language, username, encoder.encode(password), mail, UserStatus.ACTIVE.getValue());
        if (!opUser.isPresent()) {
            LOGGER.warn("User DAO returned empty user");
            return opUser;
        }
        return opUser;
    }

    @Override
    public Optional<User> findByMail(String language, String mail) {
        return userDao.findByMail(language, mail);
    }

    @Override
    public Optional<User> updatePassword(String language, String oldPassword, String newPassword, long id) throws InvalidPasswordException {
        Optional<User>opUser = findById(language, id);
        if(!opUser.isPresent()){
            LOGGER.warn("DAO could not find user");
            return Optional.empty();
        }
        if(oldPassword != null){
            LOGGER.debug("Checking old password");
            if(! encoder.matches(oldPassword, opUser.get().getPassword())){
                LOGGER.warn("Password does not match the current one");
                throw new InvalidPasswordException("Password does not match the current one");
            }
        }
        LOGGER.debug("Valid old password");
        if(userDao.updatePassword(encoder.encode(newPassword), id)){
            LOGGER.debug("Password updated");
            return userDao.findById(language, id);
        }
        LOGGER.warn("DAO could not update password");
        return Optional.empty();
    }

    @Override
    public boolean createToken(UUID uuid, long userId) {
        Date tomorrow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tomorrow);
        cal.add(Calendar.DATE, 1);
        tomorrow = cal.getTime();
        return userDao.createToken(uuid,userId,tomorrow);
    }

    @Override
    public Optional<User> activateAccountWithToken(String language, UUID uuid) {
        LOGGER.debug("Activating account token {}", uuid);

        Optional<Token> opToken = getToken(uuid);
        if (!opToken.isPresent()) {
            LOGGER.warn("Token {} not found", uuid);
            return Optional.empty();
        }
        final Token token = opToken.get();
        if (new Date().after(token.getExpirationDate())) {
            LOGGER.warn("Token {} has expired", uuid);
            return Optional.empty();
        }

        Optional<User> opUser = findByToken(language, uuid);
        if (!opUser.isPresent()) {
            LOGGER.warn("User of token {} not found", uuid);
            return Optional.empty();
        }

        if(!userDao.updateStatus(opUser.get().getId(), UserStatus.ACTIVE.getValue())) {
            LOGGER.warn("Could not activate user {} account", opUser.get().getId());
            return Optional.empty();
        }
        deleteToken(uuid);
        return opUser;
    }

    @Override
    public Optional<User> requestPasswordReset(String locale, String mail) {
        LOGGER.debug("Requesting password reset for mail {}", mail);

        Optional<User> opUser = userDao.findByMail(locale, mail);
        if (!opUser.isPresent()) {
            LOGGER.debug("User with mail {} not found", mail);
            return Optional.empty();
        }
        final User user = opUser.get();

        UUID uuid = UUID.randomUUID();
        createToken(uuid, user.getId());

        Map<String, Object> arguments = new HashMap<>();
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7/";
        String urlToken = "http://pawserver.it.itba.edu.ar/paw-2020a-7/password-reset";
        urlToken += "?token=" + uuid;

        arguments.put("URLToken", urlToken );
        arguments.put("URL", url );
        arguments.put("username",user.getUsername());

        mailService.sendMail(user.getMail(), arguments, "reset_password");
        return opUser;
    }

    @Override
    public Optional<User> resetPassword(String language, UUID uuid, String password) {
        LOGGER.debug("Resetting password for token {}", uuid);

        Optional<Token> opToken = getToken(uuid);
        if (!opToken.isPresent()) {
            LOGGER.warn("Token {} not found", uuid);
            return Optional.empty();
        }
        final Token token = opToken.get();
        if (new Date().after(token.getExpirationDate())) {
            LOGGER.warn("Token {} has expired", uuid);
            return Optional.empty();
        }

        Optional<User> opUser = findByToken(language, uuid);
        if (!opUser.isPresent()) {
            LOGGER.warn("User of token {} not found", uuid);
            return Optional.empty();
        }
        final User user = opUser.get();
        try {
            updatePassword(language,null, password, user.getId());
        }
        catch(InvalidPasswordException ignored){}
        deleteToken(uuid);

        return opUser;
    }

    @Override
    public boolean isAdmin(long userId) {
        return userDao.isAdmin(userId);
    }

    @Transactional
    @Override
    public void removeAdmin(long userId) {
        requestService.rejectAllByPetOwner(userId); //cancels all (pending) requests made to pets this user owns
        requestService.cancelAllByOwner(userId);
        petService.removeAllByOwner(userId);
        userDao.updateStatus(userId, UserStatus.DELETED.getValue());
    }

    @Transactional
    public void removeUser(long userId) {
        requestService.rejectAllByPetOwner(userId); //cancels all (pending) requests made to pets this user owns
        requestService.cancelAllByOwner(userId);
        petService.removeAllByOwner(userId);
        userDao.updateStatus(userId, UserStatus.DELETED.getValue());
    }

    @Override
    public void recoverAdmin(long userId) {
        userDao.updateStatus(userId, UserStatus.ACTIVE.getValue());
    }

    @Override
    public Optional<User> update(String language, long id, String username) throws DuplicateUserException {

        LOGGER.debug("Attempting user {} update with username: {}", id, username);
        userDao.update(language, id, username);
        Optional<User> opUser = findById(language, id);
        if (!opUser.isPresent()) {
            LOGGER.warn("Error finding user with id {}", id);
            return opUser;
        }
        LOGGER.debug("Successfully updated user; id: {} username: {}", opUser.get().getId(), opUser.get().getUsername());
        return opUser;
    }

    @Override
    public Optional<Token> getToken(UUID uuid) {
        return userDao.getToken(uuid);
    }

    @Override
    public boolean deleteToken(UUID uuid) {
        return userDao.deleteToken(uuid);
    }

    @Override
    public Optional<User> findByToken(String language, UUID uuid) {
        return userDao.findByToken(language, uuid);
    }

    @Override
    public String getAdminUserPages(){
        return userDao.getAdminPages();
    }

    @Override
    public String getAdminMaxSearchPages(String language, String find) {
        return userDao.getAdminSearchPages(language, find);
    }

    @Override
    public String getAdminMaxFilterPages(String language, String status) {
        return userDao.getAdminMaxFilterPages(language, status);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void cleanOldTokens() {
        userDao.cleanOldTokens();
    }
}