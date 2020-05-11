package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final int ACTIVE = 1;
    private final int INACTIVE = 2;
    private final int DELETED = 3;


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
    public List<User> adminUserList(String language, String page){
        return userDao.adminUserList(language, page).collect(Collectors.toList());
    }

    @Override
    public List<User> adminSearchList(String language, String find, String page) {
        return userDao.adminSearchList(language,find,page).collect(Collectors.toList());
    }

    @Override
    public Optional<User> create(String language, String username, String password, String mail, String phone) throws DuplicateUserException {
        LOGGER.debug("Attempting user creation with username: {}, mail: {}, phone: {}", username, mail, phone);
        Optional<User> opUser = userDao.create(language, username, encoder.encode(password), mail, phone, INACTIVE);
        if (!opUser.isPresent()) {
            LOGGER.warn("User DAO returned empty user");
            return opUser;
        }

        User user = opUser.get();

        UUID uuid = UUID.randomUUID();
        createToken(uuid, opUser.get().getId());
        mailService.sendMail(user.getMail(), activateAccountSubject(language), activateAccountBody(language, user, uuid));

        LOGGER.debug("Successfully created user; id: {} username: {},  mail: {}, phone: {}", user.getId(), user.getUsername(), user.getMail(), user.getPhone());
        return opUser;
    }

    @Override
    public Optional<User> adminCreate(String language, String username, String password, String mail, String phone) throws DuplicateUserException {
        LOGGER.debug("Attempting user creation with username: {}, mail: {}, phone: {}", username, mail, phone);
        Optional<User> opUser = userDao.create(language, username, encoder.encode(password), mail, phone, ACTIVE);
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
    public boolean updatePassword(String newPassword, long id) {
        return userDao.updatePassword(encoder.encode(newPassword), id);
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
        /* TODO activate account */

        if(!userDao.updateStatus(opUser.get().getId(), ACTIVE)) {
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
        mailService.sendMail(user.getMail(),resetPasswordSubject(locale),resetPasswordBody(locale, user,uuid));

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

        updatePassword(password, user.getId());
        deleteToken(uuid);

        return opUser;
    }

    @Override
    public boolean isAdmin(long userId) {
        return userDao.isAdmin(userId);
    }

    @Override
    public void removeUser(long userId) {
        requestService.cancelAllByPetOwner(userId); //cancels all (pending) requests made to pets this user owns
        requestService.cancelAllByOwner(userId);
        petService.removeAllByOwner(userId);
        userDao.updateStatus(userId, DELETED);
    }

    @Override
    public Optional<User> update(String language, long id, String username, String phone) throws DuplicateUserException {
        LOGGER.debug("Attempting user {} update with username: {}, phone: {}", id, username, phone);
        userDao.update(language, id, username, phone);
        Optional<User> opUser = findById(language, id);
        if (!opUser.isPresent()) {
            LOGGER.warn("Error finding user with id {}", id);
            return opUser;
        }
        LOGGER.debug("Successfully updated user; id: {} username: {}, phone: {}", opUser.get().getId(), opUser.get().getUsername(), opUser.get().getPhone());
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

    private String resetPasswordBody(String locale, User user, UUID uuid) {
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7/password-reset";
        url += "?token=" + uuid;
        String body;
        if(locale.equals("en_US")) {
            body = "Hello " + user.getUsername() +
                    ",\nPlease click the link below to reset your password\n"
                    + url +
                    "\nSincerely,\nPet Society Team.";
        }
        else{
            body = "Hola " + user.getUsername() +
                    ",\nPor favor haz click en el siguiente link para resetear tu contraseña\n"
                    + url +
                    "\nSinceramente,\nEl equipo de Pet Society.";
        }
        return body;
    }

    private String resetPasswordSubject(String locale) {
        String subject;
        if(locale.equals("en_US")) {
            subject = "Reset Your Password";
        }
        else { subject = "Resetea tu contraseña"; }
        return subject;
    }

    private String activateAccountSubject(String locale) {
        String subject;
        if(locale.equals("en_US")) {
            subject = "Activate your account";
        }
        else { subject = "Activa tu cuenta"; }
        return subject;
    }

    private String activateAccountBody(String locale, User user, UUID uuid) {
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7/account-activation";
        url += "?token=" + uuid;
        String body;
        if(locale.equals("en_US")) {
            body = "Hello " + user.getUsername() +
                    ",\nPlease click the link below to activate your account\n"
                    + url +
                    "\nSincerely,\nPet Society Team.";
        }
        else{
            body = "Hola " + user.getUsername() +
                    ",\nPor favor haz click en el siguiente link para activar tu cuenta\n"
                    + url +
                    "\nSinceramente,\nEl equipo de Pet Society.";
        }
        return body;
    }
}