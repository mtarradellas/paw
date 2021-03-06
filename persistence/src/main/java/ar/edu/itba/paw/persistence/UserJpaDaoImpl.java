package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortTermination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.Token;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.models.constants.UserStatus;

@Repository
public class UserJpaDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJpaDaoImpl.class);

    private static final int MAX_STATUS = 3;
    private static final int MAX_QUANTITY_OF_STATUS = 4;

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
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<User> query = em.createQuery("from User where id IN :filteredIds", User.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public List<User> searchList(List<String> find, UserStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status, searchCriteria, searchOrder);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);
    }

    private org.hibernate.search.jpa.FullTextQuery searchIdsQuery(List<String> find, UserStatus status, String searchCriteria, String searchOrder) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(User.class)
                .get();

        BooleanJunction<BooleanJunction> boolJunction = queryBuilder.bool();
        if(status != null) {
            boolJunction.must(queryBuilder.range().onField("status").below(status.getValue()).createQuery());
            boolJunction.must(queryBuilder.range().onField("status").above(status.getValue()).createQuery());
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
        SortTermination sort;
        if (searchCriteria != null ) {
            String orderBy;
            if (searchCriteria.toLowerCase().contains("mail")) orderBy = "mail";
            else orderBy = "username";

            if (searchOrder.toLowerCase().contains("desc")) {
                sort = queryBuilder.sort().byField(orderBy).desc().andByField("eid");
            }
            else {
                sort = queryBuilder.sort().byField(orderBy).asc().andByField("eid");
            }
        }
        else sort = queryBuilder.sort().byField("eid");
        jpaQuery.setSort(sort.createSort());
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
    public Set<Integer> searchStatusList(List<String> find,  UserStatus status) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status, null, null);
        jpaQuery.setProjection("status");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new TreeSet<>();
        Set<Integer> statuses = new TreeSet<>();
        for (Object[] object:results) {
            statuses.add((Integer)object[0]);
            if(statuses.size() == MAX_QUANTITY_OF_STATUS) return statuses;
        }
        return statuses;
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM users");
        Number n = (Number) nativeQuery.getSingleResult();
        if (n == null) return 0;
        return n.intValue();
    }

    @Override
    public int getSearchAmount(List<String> find, UserStatus status) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsQuery(find, status, null, null);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
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
        return query.getResultList().stream().findFirst();
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
        try {
            em.flush();
        } catch (PersistenceException ex) {
            throw new UserException(ex.getCause().getCause().getMessage());
        }
        // indexUsers();
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        em.persist(user);
        indexUsers();
        return Optional.of(user);
    }

    @Override
    public List<Review> reviewList(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status, String criteria,
                                   String order, int page, int pageSize) {

        Query nativeQuery = reviewListQuery("id", userId, targetId, minScore, maxScore, status);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Review> query = em.createQuery("from Review where id IN :filteredIds", Review.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public int getReviewListAmount(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status) {

        Query nativeQuery = reviewListQuery("count(*)", userId, targetId, minScore, maxScore, status);
        Number n = (Number) nativeQuery.getSingleResult();
        if (n == null) return 0;
        return n.intValue();
    }

    private Query reviewListQuery(String select, Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status) {
        StringBuilder str = new StringBuilder("SELECT " + select + " FROM reviews WHERE ");
        str.append("score >= :min ");
        str.append("AND score <= :max ");
        if (userId != null) str.append("AND ownerId = :owner ");
        if (targetId != null) str.append("AND targetId = :target ");
        if (status != null) str.append("AND status = :status ");

        Query nativeQuery = em.createNativeQuery(str.toString());
        nativeQuery.setParameter("min", minScore);
        if (maxScore == -1) maxScore = 5;
        nativeQuery.setParameter("max", maxScore);
        if (userId != null) nativeQuery.setParameter("owner", userId);
        if (targetId != null) nativeQuery.setParameter("target", targetId);
        if (status != null) nativeQuery.setParameter("status", status.ordinal());

        return nativeQuery;
    }

    @Override
    public Optional<Review> findReviewById(long id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public Review addReview(User owner, User target, int score, String description, ReviewStatus status) {
        final Review review = new Review(owner, target, score, description, status, LocalDateTime.now());
        em.persist(review);
        return review;
    }

    @Override
    public Optional<Review> updateReview(Review review) {
        em.persist(review);
        return Optional.of(review);
    }

    @Override
    public boolean canReview(User user, User target) {
        String qStr = "SELECT count(*) FROM requests WHERE status = :status AND ownerid = :owner AND targetid = :target";
        Query query = em.createNativeQuery(qStr);
        query.setParameter("status", RequestStatus.SOLD.getValue());
        query.setParameter("owner", user.getId().intValue());
        query.setParameter("target", target.getId().intValue());
        Number r = (Number)query.getResultList().stream().findAny().orElse(null);
        return r != null && r.intValue() > 0;
    }

    @Override
    public boolean hasReviewed(User user, User target) {
        String qStr = "SELECT count(*) FROM reviews WHERE status = :status AND ownerid = :owner AND targetid = :target";
        Query query = em.createNativeQuery(qStr);
        query.setParameter("status", ReviewStatus.VALID.getValue());
        query.setParameter("owner", user.getId().intValue());
        query.setParameter("target", target.getId().intValue());
        Number r = (Number)query.getResultList().stream().findAny().orElse(null);
        return r != null && r.intValue() > 0;
    }



    @Override
    public double getReviewAverage(Long userId, Long targetId, int minScore, int maxScore, ReviewStatus status) {
        String qStr = "SELECT AVG(score) FROM reviews WHERE score >= :minScore AND score <= :maxScore";
        if (userId != null) {
            qStr += " AND ownerId = :userId";
        }
        if (targetId != null) {
            qStr += " AND targetId = :targetId";
        }
        if (status != null) {
            qStr += " AND status = :status";
        }
        Query query = em.createNativeQuery(qStr);
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (targetId != null) {
            query.setParameter("targetId", targetId);
        }
        if (status != null) {
            query.setParameter("status", status.getValue());
        }
        query.setParameter("minScore", minScore);
        query.setParameter("maxScore", maxScore);
        Number av = (Number)query.getSingleResult();
        if (av == null) return -1;
        return av.doubleValue();
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
    public Optional<Token> createToken(UUID uuid, User user, LocalDateTime expirationDate) {
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

    private void indexUsers() {
        final LocalDateTime time = LocalDateTime.now();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch(InterruptedException ignored) {}
        final long diff = time.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        LOGGER.debug("\n\nTime Indexing: {}\n\n", diff);
    }
}
