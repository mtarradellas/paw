package ar.edu.itba.paw.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.models.Question;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.PriceRange;
import ar.edu.itba.paw.models.constants.QuestionStatus;

@Repository
public class PetJpaDaoImpl implements PetDao {

    private static final int MAX_STATUS = 4;
    private static final int MAX_RANGES = 7;
    private static final int MAX_GENDERS = 2;

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
            return new ArrayList<>();
        }
        final TypedQuery<Pet> query = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public List<Pet> searchList(String locale, List<String> find, User user, User newOwner, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                                String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize) {

        /* TODO descomentar para deployar*/
        //indexPets();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchQuery(locale, find, user, newOwner, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, searchCriteria, searchOrder);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return paginationAndOrder(locale, jpaQuery, searchCriteria, searchOrder, page, pageSize);
    }

    @Override
    public List<Breed> searchBreedList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                       PetStatus status, int minPrice, int maxPrice, Province province, Department department) {

        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchQuery(locale, find, user, null, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, null, null);

        jpaQuery.setProjection("breed.eid");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        Set<Long> filteredIds = new TreeSet<>();
        for (Object[] id:results) {
            filteredIds.add((Long) id[0]);
        }
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Breed> query2 = em.createQuery("from Breed where id in :filteredIds", Breed.class);
        query2.setParameter("filteredIds", filteredIds);
        return query2.getResultList();
    }

    @Override
    public List<Department> searchDepartmentList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                                 PetStatus status, int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchQuery(locale, find, user, null, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, null,null);

        jpaQuery.setProjection("department.eid");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        Set<Long> filteredIds = new TreeSet<>();
        for (Object[] id:results) filteredIds.add((Long) id[0]);
        if (filteredIds.size() == 0) return new ArrayList<>();
        final TypedQuery<Department> query2 = em.createQuery("from Department where id in :filteredIds", Department.class);
        query2.setParameter("filteredIds", filteredIds);
        return query2.getResultList();
    }

    @Override
    public Set<PriceRange> searchRangesList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                                 PetStatus status, int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchQuery(locale, find, user, null, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, null, null);

        jpaQuery.setProjection("price");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new TreeSet<>();
        Set<PriceRange> ranges = new TreeSet<>();
        for (Object[] price:results) {
            if(price[0].equals(PriceRange.ONE.max())) ranges.add(PriceRange.ONE);
            else if((Integer)price[0] <= PriceRange.TWO.max()) ranges.add(PriceRange.TWO);
            else if((Integer)price[0] <= PriceRange.THREE.max()) ranges.add(PriceRange.THREE);
            else if((Integer)price[0] <= PriceRange.FOUR.max()) ranges.add(PriceRange.FOUR);
            else if((Integer)price[0] <= PriceRange.FIVE.max()) ranges.add(PriceRange.FIVE);
            else if((Integer)price[0] <= PriceRange.SIX.max()) ranges.add(PriceRange.SIX);
            else if((Integer)price[0] >= PriceRange.SEVEN.min()) ranges.add(PriceRange.SEVEN);

            if(ranges.size() == MAX_RANGES) return ranges;
        }
        return ranges;
    }

    @Override
    public Set<String> searchGenderList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                        PetStatus status, int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchQuery(locale, find, user, null, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, null, null);

        jpaQuery.setProjection("gender");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new TreeSet<>();
        Set<String> genders = new TreeSet<>();
        for (Object[] object:results) {
            genders.add((String)object[0]);
            if(genders.size() == MAX_GENDERS) return genders;
        }
        return genders;
    }

    @Override
    @Deprecated
    public List<Pet> filteredList(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                                  String searchCriteria, String searchOrder, int minPrice, int maxPrice, Province province,
                                  Department department, int page, int pageSize) {

        return searchList(locale, null, user, null, species, breed, gender, status, searchCriteria, searchOrder,
                minPrice, maxPrice, province, department, page, pageSize);
    }

    @Override
    public List<Pet> listByUser(long userId, int page, int pageSize) {
        String qStr = "SELECT id FROM pets where ownerId = :owner";
        Query nativeQuery = em.createNativeQuery(qStr);
        nativeQuery.setParameter("owner", userId);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        if(filteredIds.size() == 0){
            return new ArrayList<>();
        }
        final TypedQuery<Pet> query = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    private void indexPets() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch(InterruptedException ignored) {}
    }

    private List<Pet> paginationAndOrder(String locale, Query query, String searchCriteria, String searchOrder, int page, int pageSize) {
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

        //Obtain pets with the filtered ids and sort
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

    private org.hibernate.search.jpa.FullTextQuery searchQuery(String locale, List<String> find, User user, User newOwner, Species species,
                                                                  Breed breed, String gender, PetStatus status, int minPrice,
                                                                  int maxPrice, Province province,
                                                                  Department department, String searchCriteria, String searchOrder) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Pet.class)
                .get();
        locale = locale.toLowerCase();
        String speciesField = "species." + locale;
        String breedField = "breed." + locale;

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
                        .onFields(speciesField, breedField, "gender", "petName", "province.name", "department.name")
                        .ignoreAnalyzer()
                        .matching(value)
                        .createQuery());
            }
        }
        if(user != null)  boolJunction.must(queryBuilder.keyword().onField("user.eid").matching(user.getId()).createQuery());
        if(newOwner != null)  boolJunction.must(queryBuilder.keyword().onField("newOwner.eid").matching(newOwner.getId()).createQuery());
        if(species != null)  boolJunction.must(queryBuilder.keyword().onField("species.eid").matching(species.getId()).createQuery());
        if(breed != null)  boolJunction.must(queryBuilder.keyword().onField("breed.eid").matching(breed.getId()).createQuery());
        if(gender != null)boolJunction.must(queryBuilder.keyword().onField("gender").matching(gender).createQuery());
        if(province != null)  boolJunction.must(queryBuilder.keyword().onField("province.eid").matching(province.getId()).createQuery());
        if(department != null)  boolJunction.must(queryBuilder.keyword().onField("department.eid").matching(department.getId()).createQuery());
        if(maxPrice != -1)boolJunction.must(queryBuilder.range().onField("price").below(maxPrice).createQuery());
        boolJunction.must(queryBuilder.range().onField("price").above(minPrice).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();
        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Pet.class);

        SortTermination sort;
        if (searchCriteria != null ) {
            String orderBy;
            locale = locale.toLowerCase();
            if (searchCriteria.toLowerCase().contains("gender")) orderBy = "gender";
            else if (searchCriteria.toLowerCase().contains("species")) orderBy = "species." + locale;
            else if (searchCriteria.toLowerCase().contains("breed")) orderBy = "breed." + locale;
            else if (searchCriteria.toLowerCase().contains("price")) orderBy = "price";
            else orderBy = "uploadDate";

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
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM pets where status = 0");
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public int getSearchListAmount(String locale, List<String> find, User user, User newOwner, Species species, Breed breed, String gender, PetStatus status,
                                   int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery query = searchQuery(locale, find, user, newOwner, species, breed, gender, status,
                minPrice,  maxPrice, province,  department, null, null);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results.size();
    }

    @Override
    @Deprecated
    public int getFilteredListAmount(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Province province, Department department) {
        return getSearchListAmount(locale, null, user, null, species, breed, gender, status, minPrice,  maxPrice, province, department);
    }

    @Override
    public int getListByUserAmount(long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM pets where ownerId = :owner");
        nativeQuery.setParameter("owner", userId);
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public Optional<Pet> findById(long id) {
        return Optional.ofNullable(em.find(Pet.class, id));
    }

    @Override
    public Pet create(String petName, LocalDateTime birthDate, String gender, boolean vaccinated, int price, LocalDateTime uploadDate, String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {
        Pet pet = new Pet(petName, birthDate, gender, vaccinated, price, uploadDate, description, status, user, species, breed,
                province, department);
        em.persist(pet);
        return pet;
    }

    @Override
    public Optional<Pet> update(Pet pet) {
        em.persist(pet);
        //indexPets();
        return Optional.of(pet);
    }

    @Override
    public void updateByStatusAndOwner(User user, PetStatus oldStatus, PetStatus newStatus) {
        String qStr = "update Pet set status = :new where user.id = :user and status = :old";
        Query query = em.createQuery(qStr);
        query.setParameter("old", oldStatus.getValue());
        query.setParameter("new", newStatus.getValue());
        query.setParameter("user", user.getId());
        query.executeUpdate();
        indexPets();
    }

    @Override
    public List<String> autocompleteFind(String locale, String find) {
        return new ArrayList<>();
    }

    @Override
    public List<Question> listQuestions(long petId, int page, int pageSize) {
        String qStr = "SELECT id FROM Questions where petId = :pet";
        Query nativeQuery = em.createNativeQuery(qStr);
        nativeQuery.setParameter("pet", petId);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Question> query = em.createQuery("from Question where id in :filteredIds", Question.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public int getListQuestionsAmount(long petId) {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM Questions where petId = :pet");
        nativeQuery.setParameter("pet", petId);
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public Optional<Question> findQuestionById(long questionId) {
        return Optional.ofNullable(em.find(Question.class, questionId));
    }

    @Override
    public Optional<Answer> findAnswerById(long answerId) {
        return Optional.ofNullable(em.find(Answer.class, answerId));
    }

    @Override
    public Question createQuestion(String content, User user, User target, Pet pet, QuestionStatus status) {
        Question question = new Question(content, user, target, pet, LocalDateTime.now(), status);
        em.persist(question);
        return question;
    }

    @Override
    public Answer createAnswer(Question question, String content, User user, User target, Pet pet, QuestionStatus status) {
        Answer answer = new Answer(question, content, user, target, pet, LocalDateTime.now(), status);
        em.persist(answer);
        return answer;
    }
}