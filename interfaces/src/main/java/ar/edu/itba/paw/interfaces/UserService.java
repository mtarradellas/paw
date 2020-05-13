package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserList;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {
    Optional<User> findById(String language, long id);
    Optional<User> findByUsername(String language, String username);
    Stream<User> list(String language);
    UserList adminUserList(String language, String findValue, String status, String searchCriteria, String searchOrder, String page);
    UserList adminFind(String language, String find, String page);
    UserList adminFilteredList(String language,String status,String searchCriteria,String searchOrder,String page);
    Optional<User> create(String language, String username, String password, String mail, String phone) throws DuplicateUserException;
    Optional<User> adminCreate(String language, String username, String password, String mail, String phone) throws DuplicateUserException;
    Optional<User> findByMail(String language, String mail);
    Optional<User> updatePassword(String language, String oldPassword, String newPassword, long id) throws InvalidPasswordException;
    boolean createToken(UUID uuid, long userId);
    Optional<Token> getToken(UUID uuid);
    boolean deleteToken(UUID uuid);
    Optional<User> findByToken(String language, UUID uuid);
    String getAdminUserPages();
    String getAdminMaxSearchPages(String language, String find);
    String getAdminMaxFilterPages(String language,String status);
    Optional<User> activateAccountWithToken(String language, UUID uuid);
    Optional<User> requestPasswordReset(String locale, String mail);
    Optional<User> resetPassword(String language, UUID uuid, String password);
    boolean isAdmin(long userId);
    void removeAdmin(long userId);
    void recoverAdmin(long userId);
    void removeUser(long userId);
    Optional<User> update(String language, long id, String username, String phone) throws DuplicateUserException;
}
