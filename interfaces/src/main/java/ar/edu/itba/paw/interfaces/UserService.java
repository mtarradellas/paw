package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.List;

public interface UserService {
    User findById(long id);
    User findByUsername(String username);
    List<User> list();
}
