package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserDao {
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Stream<User> list();
    User create(String username, String mail, String phone);
}
