package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {
    Optional<User> findById(String language, long id);
    Optional<User> findByUsername(String language, String username);
    Stream<User> list(String language);
    List<User> adminUserList(String language, String page);
    List<User> adminSearchList(String language, String find, String page);
    Optional<User> create(String language, String username, String password, String mail, String phone) throws DuplicateUserException;
    Optional<User> adminCreate(String language, String username, String password, String mail, String phone) throws DuplicateUserException;
    Optional<User> findByMail(String language, String mail);
    boolean updatePassword(String newPassword, long id);
    boolean createToken(UUID uuid, long userId);
    Optional<Token> getToken(UUID uuid);
    boolean deleteToken(UUID uuid);
    Optional<User> findByToken(String language, UUID uuid);
    String getAdminUserPages();
    String getAdminMaxSearchPages(String language, String find);
    Optional<User> activateAccountWithToken(String language, UUID uuid);
    Optional<User> requestPasswordReset(String locale, String mail);
    Optional<User> resetPassword(String language, UUID uuid, String password);
    boolean isAdmin(long userId);
    void removeAdmin(long userId);
    void recoverAdmin(long userId);
    void removeUser(long userId);
}
