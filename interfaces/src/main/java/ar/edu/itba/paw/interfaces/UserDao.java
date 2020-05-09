package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserDao {
    Optional<User> findById(String language, long id);
    Optional<User> findByUsername(String language, String username);
    Stream<User> list(String language);
    Stream<User> adminUserList(String language, String page);
    Stream<User> adminSearchList(String language, String find, String page);
    Optional<User> create(String language, String username, String password, String mail, String phone, int status) throws DuplicateUserException;
    Optional<User> findByMail(String language, String mail);
    boolean updatePassword(String newPassword, long id);
    boolean createToken(UUID uuid, long userId, Date expirationDate);
    Optional<Token> getToken(UUID uuid);
    boolean deleteToken(UUID uuid);
    Optional<User> findByToken(String language, UUID uuid);
    String getAdminPages();
    String getAdminSearchPages(String language, String find);
    boolean updateStatus(long id, int status);
    boolean isAdmin(long userId);
}
