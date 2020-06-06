package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;

import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserJpaDaoImpl implements UserDao {

    private static final int MAX_STATUS = 3;

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
        return query.getResultList();
    }

    @Override
    public List<User> searchList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status);
        return paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);
    }

    private org.hibernate.search.jpa.FullTextQuery searchIdsQuery(List<String> find, UserStatus status) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        /* TODO descomentar para deployar*/
//        try {
//            fullTextEntityManager.createIndexer().startAndWait();
//        } catch(InterruptedException ignored) {}
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(User.class)
                .get();

        BooleanJunction<BooleanJunction> boolJunction = queryBuilder.bool();
        if(status != null) {
            boolJunction.must(queryBuilder.range().onField("status").below(status.getValue() - 1).createQuery());
            boolJunction.must(queryBuilder.range().onField("status").above(status.getValue() - 1).createQuery());
        }
        else boolJunction.must(queryBuilder.range().onField("status").below(MAX_STATUS).createQuery());
        if(find != null) {
            for (String value : find) {
                boolJunction.must(queryBuilder
                        .keyword()
                        .fuzzy()
                        .withEditDistanceUpTo(1)
                        .withPrefixLength(0)
                        .onFields("username", "mail")
                        .ignoreAnalyzer()
                        .matching(value)
                        .createQuery());
            }
        }

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, User.class);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return jpaQuery;
    }

    private List<User> paginationAndOrder(Query query, String searchCriteria, String searchOrder, int page, int pageSize) {
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        List<Long> filteredIds = new ArrayList<>();
        for (Object[] id:results) {
            filteredIds.add((Long)id[0]);
        }
        if (filteredIds.size() == 0) return new ArrayList<>();

        //Obtain users with the filtered ids and sort
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root= cr.from(User.class);

        Expression<String> exp = root.get("id");
        Predicate predicate = exp.in(filteredIds);

        cr.select(root).where(predicate);
        if (searchCriteria != null ) {
            Order order;
            Path<Object> orderBy = root.get("username");
            if (searchCriteria.toLowerCase().contains("mail")) {
                orderBy = root.get("mail");
            }

            if (searchOrder.toLowerCase().contains("desc")) {
                order = cb.desc(orderBy);
            } else {
                order = cb.asc(orderBy);
            }
            cr.orderBy(order);
        }
        return em.createQuery(cr).getResultList();
    }

    @Override
    @Deprecated
    public List<User> filteredList(UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return searchList(null, status, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM users");
        return ((Number)nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public int getSearchAmount(List<String> find, UserStatus status) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsQuery(find, status);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        for (Object[] id:results) {
            System.out.println("\n\n\nWWWWWSSSSSSSSSSSSS"+ id[0]);
        }
        return results.size();
    }

    @Override
    @Deprecated
    public int getFilteredAmount(UserStatus status) {
        return getSearchAmount(null, status);
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
    public User create(String username, String password, String mail, UserStatus status, String locale) {
        final User user = new User(username, password, mail, status, locale);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        em.persist(user);
        return Optional.of(user);
    }

    @Override
    public List<Review> reviewList(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status, String criteria,
                                   String order, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public int getReviewListAmount(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status) {
        return 0;
    }

    @Override
    public Optional<Review> findReviewById(long id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public void addReview(User owner, User target, int score, String description, ReviewStatus status) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        today = cal.getTime();
        final Review review = new Review(owner, target, score, description, status, today);
        em.persist(review);
    }

    @Override
    public Optional<Review> updateReview(Review review) {
        em.persist(review);
        return Optional.of(review);
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
