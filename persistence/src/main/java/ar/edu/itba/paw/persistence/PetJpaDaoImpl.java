package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PetJpaDaoImpl implements PetDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Pet> list(int page, int pageSize) {
        String qStr = "SELECT id FROM pets where status = 0";
        Query nativeQuery = em.createNativeQuery(qStr);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        if(filteredIds.size() == 0){
            return new ArrayList<Pet>();
        }
        final TypedQuery<Pet> query = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query.setParameter("filteredIds", filteredIds);
        List<Pet> petList = query.getResultList();
        return petList;
    }

    @Override
    public List<Pet> searchList(String locale, String find, int page, int pageSize) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch(InterruptedException ignored) {}

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Pet.class)
                .get();
        locale = locale.toLowerCase();
        String species = "species." + locale;
        String breed = "breed." + locale;
        org.apache.lucene.search.Query query = queryBuilder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(0)
                .onFields(species, breed, "gender", "petName", "province.name", "department.name")
                .matching(find)
                .createQuery();
        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Pet.class);
        List<Pet> results = jpaQuery.getResultList();
        return results;
    }

    @Override
    public List<Pet> filteredList(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                                  String searchCriteria, String searchOrder, int minPrice, int maxPrice, Province province,
                                  Department department, int page, int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pet> cr = cb.createQuery(Pet.class);
        Root<Pet> root = cr.from(Pet.class);

        List<Predicate> predicates = predicatesForFilteredList(user, status, species, breed, gender, minPrice,
                                                                maxPrice, province, department, cb, root);

        cr.select(root.get("id")).where(cb.and(predicates.toArray(new Predicate[] {})));
        Query query = em.createQuery(cr);
        return filteredPagination(locale, query, searchCriteria, searchOrder, page, pageSize);
    }

    private List<Predicate> predicatesForFilteredList (User user, PetStatus status, Species species, Breed breed,
                                                       String gender, int minPrice, int maxPrice,
                                                       Province province, Department department, CriteriaBuilder cb,
                                                       Root<Pet> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (status != null) {
            Expression<PetStatus> petStatus = root.get("status");
            predicates.add(cb.equal(petStatus, status.getValue()-1));
        }
        if(user != null) {
            Expression<User> petUser = root.get("user");
            predicates.add(cb.equal(petUser, user));
        }
        if(species != null) {
            Expression<Species> petSpecies = root.get("species");
            predicates.add(cb.equal(petSpecies, species));
        }
        if(breed != null) {
            Expression<Breed> petBreed = root.get("breed");
            predicates.add(cb.equal(petBreed, breed));
        }
        if(gender != null) {
            Expression<String> petGender = root.get("gender");
            predicates.add(cb.equal(petGender, gender));
        }
        predicates.add(cb.ge(root.get("price"), minPrice));
        if(maxPrice != -1) {
            predicates.add(cb.le(root.get("price"), maxPrice));
        }
        if(province != null) {
            Expression<Province> petProvince = root.get("province");
            predicates.add(cb.equal(petProvince, province));
        }
        if(department != null) {
            Expression<Department> petDepartment = root.get("department");
            predicates.add(cb.equal(petDepartment, department));
        }
        return predicates;
    }

    private List<Pet> filteredPagination(String locale, Query query, String searchCriteria, String searchOrder, int page, int pageSize) {
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = query.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        if (filteredIds.size() == 0){
            return new ArrayList<>();
        }

        //Obtain Requests with the filtered ids and sort
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pet> cr = cb.createQuery(Pet.class);
        Root<Pet> root= cr.from(Pet.class);

        Expression<String> exp = root.get("id");
        Predicate predicate = exp.in(filteredIds);

        cr.select(root).where(predicate);
        if (searchCriteria != null ) {
            Order order;
            locale = locale.toLowerCase();
            Path<Object> orderBy = root.get("uploadDate");
            if (searchCriteria.toLowerCase().contains("gender")) {
                orderBy = root.get("gender");
            }
            else if (searchCriteria.toLowerCase().contains("species")) {
                orderBy = root.join("species").get(locale);
            }
            else if (searchCriteria.toLowerCase().contains("breed")) {
                orderBy = root.join("breed").get(locale);
            }
            else if (searchCriteria.toLowerCase().contains("price")) {
                orderBy = root.get("price");
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
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM pets where status = 0");
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public int getSearchListAmount(String locale, String find) {
        return searchList(locale,find,-1,-1).size();
    }

    @Override
    public int getFilteredListAmount(User user, Species species, Breed breed, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Province province, Department department) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Pet> root = cr.from(Pet.class);

        List<Predicate> predicates = predicatesForFilteredList(user, status, species, breed, gender, minPrice,
                                                                maxPrice, province, department, cb, root);

        cr.select(cb.count(root.get("id"))).where(cb.and(predicates.toArray(new Predicate[] {})));
        return em.createQuery(cr).getSingleResult().intValue();
    }

    @Override
    public Optional<Pet> findById(long id) {
        return Optional.ofNullable(em.find(Pet.class, id));
    }

    @Override
    public Pet create(String petName, Date birthDate, String gender, boolean vaccinated, int price, Date uploadDate, String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {
        Pet pet = new Pet(petName, birthDate, gender, vaccinated, price, uploadDate, description, status, user, species, breed,
                province, department);
        em.persist(pet);
        return pet;
    }

    @Override
    public Optional<Pet> update(Pet pet) {
        em.persist(pet);
        return Optional.of(pet);
    }

    @Override
    public void updateByStatusAndOwner(User user, PetStatus oldStatus, PetStatus newStatus) {
        String qStr = "update Pet set status = :new where user.id = :user and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus);
        query.setParameter("new", newStatus);
        query.setParameter("user", user.getId());
        query.executeUpdate();
    }

    @Override
    public List<String> autocompleteFind(String locale, String find) {
        return new ArrayList<>();
    }
}