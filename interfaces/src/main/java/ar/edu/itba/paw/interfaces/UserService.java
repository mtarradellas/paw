package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ar.edu.itba.paw.interfaces.exceptions.InvalidPasswordException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.UserStatus;

public interface UserService {

    List<User> list(int page, int pageSize);
    List<User> filteredList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);
    List<UserStatus> filteredStatusList(List<String> find, UserStatus status);
    int getListAmount();
    int getFilteredAmount(List<String> find, UserStatus status);

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByToken(UUID token);

    Optional<User> create(String username, String password, String mail, String locale, String contextURL);
    Optional<User> update(User user);
    Optional<User> updateUsername(long id, String username);
    Optional<User> updateStatus(long id, UserStatus status);
    Optional<User> updateLocale(long id, String locale);
    Optional<User> updateLocale(User user, String locale);
    Optional<User> updatePassword(long id, String oldPassword, String newPassword) throws InvalidPasswordException;
    Optional<User> requestPasswordReset(String mail, String contextURL);
    Optional<User> resetPassword(UUID token, String password);
    String getMail(User user, long userId);

    Optional<User> adminCreate(String username, String password, String mail, String locale);
    boolean isAdmin(User user);
    boolean isAdminUsername(String username);
    boolean recoverUser(long id);
    boolean removeUser(long id);

    Optional<Token> getToken(UUID token);
    Optional<Token> createToken(UUID token, User user);
    boolean deleteToken(UUID token);
    Optional<User> activateAccountWithToken(UUID token);
    void cleanOldTokens();
}
