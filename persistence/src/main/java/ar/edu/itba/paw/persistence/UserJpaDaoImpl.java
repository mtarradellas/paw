package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Status;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserJpaDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> list(int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("SELECT id FROM users");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        final TypedQuery<User> query = em.createQuery("from User where id IN :filteredIds", User.class);
        query.setParameter("filteredIds", filteredIds);
        List<User> userList = query.getResultList();
        System.out.println("lenia");
        filteredIds.forEach(System.out::println);
        userList.forEach(System.out::println);
        return userList;
    }

    @Override
    public List<User> searchList(String find, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public List<User> filteredList(UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM users");
        return nativeQuery.getFirstResult();
    }

    @Override
    public int getSearchAmount(String find) {
        return 0;
    }

    @Override
    public int getFilteredAmount(UserStatus status) {
        return 0;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String qStr = "from User as u where u.username = :username";
        final TypedQuery<User> query = em.createQuery(qStr, User.class);
        query.setParameter("username", username);
        User user = query.getSingleResult();
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByMail(String mail) {
        final String qStr = "from User as u where u.mail = :mail";
        final TypedQuery<User> query = em.createQuery(qStr, User.class);
        query.setParameter("mail", mail);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByToken(UUID token) {
        final String qStr = "from Token as t where t.token = :token";
        final TypedQuery<Token> query = em.createQuery(qStr, Token.class);
        query.setParameter("token", token);
        Optional<Token> opToken =  query.getResultList().stream().findFirst();
        return opToken.map(Token::getUser);
    }

    @Override
    public User create(String username, String password, String mail, UserStatus status) {
        final User user = new User(username, password, mail, status);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        em.persist(user);
        return Optional.of(user);
    }

    @Override
    public boolean isAdmin(User user) {
        Query nativeQuery = em.createNativeQuery("SELECT id FROM admins WHERE id = :id");
        nativeQuery.setParameter("id", user.getId());
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        return resultList.stream().findFirst().isPresent();
    }

    @Override
    public List<Token> listTokens() {
        return em.createQuery("from Token", Token.class).getResultList();
    }

    @Override
    public Optional<Token> findToken(UUID token) {
        final String qStr = "from Token as t where t.token = :token";
        final TypedQuery<Token> query = em.createQuery(qStr, Token.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Token> createToken(UUID uuid, User user, Date expirationDate) {
        Token token = new Token(uuid, expirationDate, user);
        em.persist(token);
        return Optional.of(token);
    }

    @Override
    public boolean deleteToken(UUID token) {
        String qStr = "delete Token where token = :token";
        Query query = em.createQuery(qStr);
        query.setParameter("token", token);
        return query.executeUpdate() > 0;
    }

    @Override
    public void cleanOldTokens() {
        String qStr = "delete Token where expirationDate < current_date ";
        Query query = em.createQuery(qStr);
        query.executeUpdate();
    }
}
