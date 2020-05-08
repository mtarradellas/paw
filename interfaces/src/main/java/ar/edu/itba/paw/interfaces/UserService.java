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
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Stream<User> list();
    List<User> adminUserList(String page);
    List<User> adminSearchList(String language, String find, String page);
    Optional<User> create(String username, String password, String mail, String phone) throws DuplicateUserException;
    Optional<User> findByMail(String mail);
    boolean updatePassword(String newPassword, long id);
    boolean createToken(UUID uuid, long userId);
    Optional<Token> getToken(UUID uuid);
    boolean deleteToken(UUID uuid);
    Optional<User> findByToken(UUID uuid);
    String getAdminUserPages();
    String getAdminMaxSearchPages(String language, String find);
}
