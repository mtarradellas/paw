package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> findById(long id) {
        return this.userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Override
    public Stream<User> list() {
        return this.userDao.list();
    }

    @Override
    public User create(String username, String password, String mail, String phone) {
        return this.userDao.create(username, encoder.encode(password), mail, phone);
    }
}