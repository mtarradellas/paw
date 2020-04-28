package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
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
    public Optional<User> create(String username, String password, String mail, String phone) throws DuplicateUserException {
        return this.userDao.create(username, encoder.encode(password), mail, phone);
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return userDao.findByMail(mail);
    }

    @Override
    public boolean updatePassword(String newPassword) {
        return userDao.updatePassword(newPassword);
    }

    @Override
    public boolean createToken(UUID uuid, long userId) {
        Date tomorrow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tomorrow);
        cal.add(Calendar.DATE, 1);
        tomorrow = cal.getTime();
        return userDao.createToken(uuid,userId,tomorrow);
    }

    @Override
    public Optional<User> findByToken(UUID uuid) {
        return userDao.findByToken(uuid);
    }
}