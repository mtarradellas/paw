package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;

@Repository
public class RequestJpaDaoImpl implements RequestDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestJpaDaoImpl.class);
    private static final int MAX_STATUS = RequestStatus.maxValue();
    private static final int MAX_QUANTITY_OF_STATUS = RequestStatus.amount();

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Request> list(int page, int pageSize) {
        String qStr = "SELECT id FROM requests";
        Query nativeQuery = em.createNativeQuery(qStr);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> ids = resultList.stream().map(Number::longValue).collect(Collectors.toList());
        if(ids.size() == 0) {
            return Collections.emptyList();
        }

        final TypedQuery<Request> query = em.createQuery("from Request where id in :filteredIds", Request.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();
    }

    @Override
    public List<Request> searchList(User user, Pet pet, List<String> find, RequestStatus status, String searchCriteria,
                                    String searchOrder, int page, int pageSize) {

        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status, user, pet, null, null);
        jpaQuery.setProjection(ProjectionConstants.ID);
        List<Request> reqs = paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);

        return reqs;
    }

    @Override
    public Set<Integer> searchStatusList(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status, user, pet, null ,null);
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
    public List<Pet> searchPetListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsByPetOwnerQuery(find, status, user, pet, null, null);
        jpaQuery.setProjection("pet.eid");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        Set<Long> filteredIds = new TreeSet<>();
        for (Object[] object:results) filteredIds.add((Long) object[0]);
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Pet> query2 = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query2.setParameter("filteredIds", filteredIds);
        return query2.getResultList();
    }

    private org.hibernate.search.jpa.FullTextQuery searchIdsQuery(List<String> find, RequestStatus status, User user, Pet pet,
                                                                  String searchCriteria, String searchOrder) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        LOGGER.debug("Preparing Lucene Query (Requests): user {}, pet {}, status {}", user, pet, status);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Request.class)
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
                        .onFields("pet.user.username","pet.petName","user.username")
                        .ignoreAnalyzer()
                        .matching(value)
                        .createQuery());
            }
        }
        if(user != null)  boolJunction.must(queryBuilder.phrase().onField("user.username").sentence(user.getUsername()).createQuery());
        if(pet != null)  boolJunction.must(queryBuilder.keyword().onField("pet.eid").matching(pet.getId()).createQuery());
        
        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Request.class);
        SortTermination sort;
        if (searchCriteria != null ) {
            String orderBy;
            if (searchCriteria.toLowerCase().contains("pet")) orderBy = "petName";
            else orderBy = "creationDate";

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

    private List<Request> paginationAndOrder(Query query, String searchCriteria, String searchOrder, int page, int pageSize) {
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        if (results.size() == 0) {
            return new ArrayList<>();
        }
        List<Long> filteredIds = new ArrayList<>();
        for (Object[] id:results) {
            filteredIds.add((Long)id[0]);
        }
        if (filteredIds.size() == 0) return new ArrayList<>();

        //Obtain Requests with the filtered ids and sort
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> cr = cb.createQuery(Request.class);
        Root<Request> root= cr.from(Request.class);

        Expression<String> exp = root.get("id");
        Predicate predicate = exp.in(filteredIds);

        cr.select(root).where(predicate);
        if (searchCriteria != null ) {
            Order order;
            Path<Object> orderBy =root.join("pet").get("petName");
            if (searchCriteria.toLowerCase().contains("date")) {
                orderBy = root.get("creationDate");
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
    public List<Request> filteredList(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return searchList(user, pet,null, status, searchCriteria, searchOrder,page, pageSize);
    }

    @Override
    public List<Request> searchListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsByPetOwnerQuery(find, status, user, pet, null, null);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public Set<Integer> searchStatusListByPetOwner(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsByPetOwnerQuery(find, status, user, pet, null, null);
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

    private org.hibernate.search.jpa.FullTextQuery searchIdsByPetOwnerQuery(List<String> find, RequestStatus status, User user,
                                                                            Pet pet, String searchCriteria, String searchOrder) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        LOGGER.debug("Preparing Lucene Query (Interests): user {}, pet {}, status {}", user, pet, status);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Request.class)
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
                        .onFields("pet.user.username","pet.petName","user.username")
                        .ignoreAnalyzer()
                        .matching(value)
                        .createQuery());
            }
        }
        if(user != null)  boolJunction.must(queryBuilder.phrase().onField("pet.user.username").sentence(user.getUsername()).createQuery());
        if(pet != null)  boolJunction.must(queryBuilder.keyword().onField("pet.eid").matching(pet.getId().toString()).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Request.class);

        SortTermination sort;
        if (searchCriteria != null ) {
            String orderBy;
            if (searchCriteria.toLowerCase().contains("pet")) orderBy = "petName";
            else orderBy = "creationDate";

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

    @Override
    @Deprecated
    public List<Request> filteredListByPetOwner(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return searchListByPetOwner(user, pet, null, status, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM requests");
        Number n = (Number) nativeQuery.getSingleResult();
        return n.intValue();
    }

    @Override
    public int getSearchListAmount(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsQuery(find, status, user, pet, null, null);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results.size();
    }

    @Override
    @Deprecated
    public int getFilteredListAmount(User user, Pet pet, RequestStatus status) {
        return getSearchListAmount(user, pet, null, status);

    }

    @Override
    public int getSearchListByPetOwnerAmount(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsByPetOwnerQuery(find, status, user, pet, null, null);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results.size();
    }

    @Override
    @Deprecated
    public int getFilteredListByPetOwnerAmount(User user, Pet pet, RequestStatus status) {
        return getSearchListByPetOwnerAmount(user, pet,null, status);
    }

    @Override
    public Optional<Request> findById(long id) {
        return Optional.ofNullable(em.find(Request.class, id));
    }

    @Override
    public Request create(User user, Pet pet, RequestStatus status, LocalDateTime creationDate) {
        Request request = new Request(creationDate, status, user, pet.getUser(), pet);
        request.setUpdateDate(LocalDateTime.now());
        em.persist(request);
        return request;
    }

    @Override
    public Optional<Request> update(Request request) {
        request.setUpdateDate(LocalDateTime.now());
        em.persist(request);
        return Optional.of(request);
    }

    @Override
    public void updateByStatusAndUser(User user, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new, updateDate = :now where (user.id = :user and status = :old)";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.ordinal());
        query.setParameter("new", newStatus.ordinal());
        query.setParameter("user", user.getId());
        query.setParameter("now", LocalDateTime.now());
        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPetOwner(User petOwner, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new, updateDate = :now where (target.id = :target and status = :old)";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.ordinal());
        query.setParameter("new", newStatus.ordinal());
        query.setParameter("target", petOwner.getId());
        query.setParameter("now", LocalDateTime.now());
        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPet(Pet pet, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new, updateDate = :now where (pet.id = :pet and status = :old)";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.ordinal());
        query.setParameter("new", newStatus.ordinal());
        query.setParameter("pet", pet.getId());
        query.setParameter("now", LocalDateTime.now());
        query.executeUpdate();
    }

    @Override
    public int interestNotifs(User user) {
        String qStr = "SELECT count(*) " +
                      "FROM requests " +
                      "WHERE targetId = :user AND updateDate > :lastOnline ";

        Query query = em.createNativeQuery(qStr);
        query.setParameter("user", user.getId());
        Timestamp date = user.getInterestsDate()==null? Timestamp.valueOf("2005-07-01 01:01:01") : Timestamp.valueOf(user.getInterestsDate());
        query.setParameter("lastOnline", date);
        Number n = (Number) query.getSingleResult();
        return n.intValue();
    }

    @Override
    public int requestNotifs(User user) {
        String qStr = "SELECT count(*) " +
                "FROM requests " +
                "WHERE ownerId = :user AND updateDate > :lastOnline ";

        Query query = em.createNativeQuery(qStr);
        query.setParameter("user", user.getId());
        Timestamp date = user.getRequestsDate()==null? Timestamp.valueOf("2005-07-01 01:01:01") : Timestamp.valueOf(user.getRequestsDate());
        query.setParameter("lastOnline", date);
        Number n = (Number) query.getSingleResult();
        return n.intValue();
    }

    @Override
    public boolean hasRequest(User user, User target, List<RequestStatus> statusList) {
        String status = String.join(", ", statusList.stream().map(s -> String.valueOf(s.getValue())).collect(Collectors.toList()));
        String qStr = "SELECT count(*) " +
                      "FROM requests " +
                      "WHERE ownerId = :user AND targetId = :target AND status in (" + status + ")";

        Query query = em.createNativeQuery(qStr);
        query.setParameter("user", user.getId());
        query.setParameter("target", target.getId());
        Number n = (Number) query.getSingleResult();
        return n.intValue() > 0;
    }

    @Override
    public boolean hasRequest(User user, Pet pet, List<RequestStatus> statusList) {
        String status = String.join(", ", statusList.stream().map(s -> String.valueOf(s.getValue())).collect(Collectors.toList()));
        String qStr = "SELECT count(*) " +
                      "FROM requests " +
                      "WHERE ownerId = :user AND petId = :pet AND status in (" + status + ")";

        Query query = em.createNativeQuery(qStr);
        query.setParameter("user", user.getId());
        query.setParameter("pet", pet.getId());
        Number n = (Number) query.getSingleResult();
        return n.intValue() > 0;
    }
}