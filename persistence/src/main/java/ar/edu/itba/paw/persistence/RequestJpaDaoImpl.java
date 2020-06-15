package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Repository
public class RequestJpaDaoImpl implements RequestDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestJpaDaoImpl.class);
    private static final int MAX_STATUS = 3;

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
            return new ArrayList<>();
        }

        final TypedQuery<Request> query = em.createQuery("from Request where id in :ids", Request.class);
        query.setParameter("filteredIds", ids);
        return query.getResultList();

    }

    @Override
    public List<Request> searchList(User user, Pet pet, List<String> find, RequestStatus status, String searchCriteria,
                                    String searchOrder, int page, int pageSize) {

        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(find, status, user, pet);
       List<Request> reqs =paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);
        return reqs;
    }

    private org.hibernate.search.jpa.FullTextQuery searchIdsQuery(List<String> find, RequestStatus status, User user, Pet pet) {
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
        //if(pet != null)  boolJunction.must(queryBuilder.phrase().onField("pet.username").sentence(user.getUsername()).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Request.class);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return jpaQuery;
    }

    private List<Request> paginationAndOrder(Query query, String searchCriteria, String searchOrder, int page, int pageSize) {
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
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsByPetOwnerQuery(find, status, user, pet);
        List<Request> reqs =paginationAndOrder(jpaQuery, searchCriteria, searchOrder, page, pageSize);
        return reqs;
    }

    private org.hibernate.search.jpa.FullTextQuery searchIdsByPetOwnerQuery(List<String> find, RequestStatus status, User user, Pet pet) {
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
        //if(pet != null)  boolJunction.must(queryBuilder.phrase().onField("pet.username").sentence(user.getUsername()).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Request.class);
        jpaQuery.setProjection(ProjectionConstants.ID);
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
        return nativeQuery.getFirstResult();
    }

    @Override
    public int getSearchListAmount(User user, Pet pet, List<String> find, RequestStatus status) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsQuery(find, status, user, pet);
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
        org.hibernate.search.jpa.FullTextQuery query = searchIdsByPetOwnerQuery(find, status, user, pet);
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
    public Request create(User user, Pet pet, RequestStatus status) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        today = cal.getTime();
        Request request = new Request(today, status, user, pet.getUser(), pet);
        em.persist(request);
        return request;
    }

    @Override
    public Optional<Request> update(Request request) {
        em.persist(request);
        return Optional.of(request);
    }

    @Override
    public void updateByStatusAndUser(User user, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new where user.id = :user and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.getValue());
        query.setParameter("new", newStatus.getValue());
        query.setParameter("user", user.getId());
        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPetOwner(User petOwner, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new where target.id = :target and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.getValue());
        query.setParameter("new", newStatus.getValue());
        query.setParameter("target", petOwner.getId());
        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPet(Pet pet, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new where pet.id = :pet and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.getValue());
        query.setParameter("new", newStatus.getValue());
        query.setParameter("pet", pet.getId());
        query.executeUpdate();
    }

    @Override
    public boolean isRequestTarget(Request request, User user) {
        return false;
    }
}
