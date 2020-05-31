package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RequestDao;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RequestJpaDaoImpl implements RequestDao {

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
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        final TypedQuery<Request> query = em.createQuery("from Request where id in :filteredIds", Request.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();

    }

    @Override
    public List<Request> searchList(User user, Pet pet, String find, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public List<Request> filteredList(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> cr = cb.createQuery(Request.class);
        Root<Request> root = cr.from(Request.class);

        List<Predicate> predicates = new ArrayList<>();
        if (status != null) {
            Expression<RequestStatus> reqStatus = root.get("status");
            predicates.add(cb.equal(reqStatus, status.getValue()-1));
        }
        if(user != null) {
            Expression<User> reqUser = root.get("user");
            predicates.add(cb.equal(reqUser, user));
        }
//        if(pet != null) {
//            Expression<Pet> reqPet = root.get("pet");
//            predicates.add(cb.equal(reqPet, pet));
//        }
        if (searchCriteria != null) {
            Order order;
            String orderBy = "creationDate";
            if(searchCriteria.toLowerCase().contains("date")){
                orderBy = "creationDate";
            }
//            else if (searchCriteria.toLowerCase().contains("petname")) {
//                orderBy = "creationDate";
//            }
            if (searchOrder.toLowerCase().contains("desc")) {
                order = cb.desc(root.get(orderBy));
            } else {
                order = cb.asc(root.get(orderBy));
            }

            cr.orderBy(order);
        }

        cr.select(root).where(cb.and(predicates.toArray(new Predicate[] {})));
        TypedQuery<Request> query = em.createQuery(cr);
        return query.getResultList();

    }

    @Override
    public List<Request> searchListByPetOwner(User user, Pet pet, String find, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public List<Request> filteredListByPetOwner(User user, Pet pet, RequestStatus status, String searchCriteria, String searchOrder, int page, int pageSize) {
        return new ArrayList<>();
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM requests");
        return nativeQuery.getFirstResult();
    }

    @Override
    public int getSearchListAmount(User user, Pet pet, String find) {
        return 0;
    }

    @Override
    public int getFilteredListAmount(User user, Pet pet, RequestStatus status) {
        return 0;
    }

    @Override
    public int getSearchListByPetOwnerAmount(User user, Pet pet, String find) {
        return 0;
    }

    @Override
    public int getFilteredListByPetOwnerAmount(User user, Pet pet, RequestStatus status) {
        return 0;
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
        Request request = new Request(today, status, user, pet.getId());
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
        query.setParameter("old", oldStatus);
        query.setParameter("new", newStatus);
        query.setParameter("user", user.getId());
        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPetOwner(User petOwner, RequestStatus oldStatus, RequestStatus newStatus) {
//        String qStr = "update Request set status = :new where pet.owner.id = :owner and status = :old";
//        Query query = em.createQuery(qStr);
//        query.setParameter("old", oldStatus);
//        query.setParameter("new", newStatus);
//        query.setParameter("owner", owner.id);
//        query.executeUpdate();
    }

    @Override
    public void updateByStatusAndPet(Pet pet, RequestStatus oldStatus, RequestStatus newStatus) {
        String qStr = "update Request set status = :new where petId = :pet and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus);
        query.setParameter("new", newStatus);
        query.setParameter("pet", pet.getId());
        query.executeUpdate();
    }

    @Override
    public boolean isRequestTarget(Request request, User user) {
        return false;
    }
}
