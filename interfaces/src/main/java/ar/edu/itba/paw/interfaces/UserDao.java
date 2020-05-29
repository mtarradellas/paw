package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.UserStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserDao {

    List<User> list(int page, int pageSize);
    List<User> searchList(String find, int page, int pageSize);
    List<User> filteredList(UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getSearchAmount(String find);
    int getFilteredAmount(UserStatus status);

    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByToken(UUID token);

    User create(String username, String password, String mail, UserStatus status);
    Optional<User> update(User user);
    boolean isAdmin(User user);

    List<Token> listTokens();
    Optional<Token> findToken(UUID token);
    Optional<Token> createToken(UUID token, User user, Date expirationDate);
    boolean deleteToken(UUID token);
    void cleanOldTokens();

}
