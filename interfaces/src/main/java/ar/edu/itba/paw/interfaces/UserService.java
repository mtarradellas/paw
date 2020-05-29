package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserList;
import ar.edu.itba.paw.models.constants.UserStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {

    List<User> list(int page, int pageSize);
    List<User> filteredList(String find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize);

    int getListAmount();
    int getFilteredAmount(String find, UserStatus status);

    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    Optional<User> findByToken(UUID token);

    Optional<User> create(String username, String password, String mail) throws DuplicateUserException;
    Optional<User> update(User user);
    Optional<User> updateUsername(User user, String username) throws DuplicateUserException;
    Optional<User> updateStatus(User user, UserStatus status);
    Optional<User> updatePassword(User user, String oldPassword, String newPassword) throws InvalidPasswordException;
    Optional<User> requestPasswordReset(String mail);
    Optional<User> resetPassword(UUID token, String password);

    Optional<User> adminCreate(String username, String password, String mail) throws DuplicateUserException;
    boolean isAdmin(User user);
    boolean recoverUser(User user);
    boolean removeUser(User user);

    Optional<Token> getToken(UUID token);
    Optional<Token> createToken(UUID token, User user);
    boolean deleteToken(UUID token);
    Optional<User> activateAccountWithToken(UUID token);

}
