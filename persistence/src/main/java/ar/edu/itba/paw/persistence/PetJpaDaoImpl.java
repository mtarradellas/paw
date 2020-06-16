package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.QuestionStatus;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PetJpaDaoImpl implements PetDao {

    private static final int MAX_STATUS = 4;

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
    public List<Pet> searchList(String locale, List<String> find, User user, Species species, Breed breed, String gender, PetStatus status, String searchCriteria,
                                String searchOrder, int minPrice, int maxPrice, Province province, Department department, int page, int pageSize) {

        /* TODO descomentar para deployar*/
//        indexPets();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(locale, find, user, species, breed, gender, status,
                minPrice,  maxPrice, province,  department);
        return paginationAndOrder(locale, jpaQuery,searchCriteria,searchOrder,page,pageSize);
    }

    @Override
    public List<Breed> searchBreedList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                       PetStatus status, int minPrice, int maxPrice, Province province, Department department) {
//        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(locale, find, user, species, breed, gender, status,
//                minPrice,  maxPrice, province,  department);
//        @SuppressWarnings("unchecked")
//        List<Object[]> results = jpaQuery.getResultList();
//        if (results.size() == 0) return new ArrayList<>();
//        List<Long> filteredPetIds = new ArrayList<>();
//        for (Object[] id:results) {
//            filteredPetIds.add((Long)id[0]);
//        }
//        if (filteredPetIds.size() == 0) return new ArrayList<>();
//        final TypedQuery<Breed> query = em.createQuery("from Breed where id in :filteredPetIds", Breed.class);
//        query.setParameter("filteredPetIds", filteredPetIds);
//
//        final TypedQuery<Breed> query = em.createQuery("from Breed where id in :filteredIds", Breed.class);
//        query.setParameter("filteredIds", filteredIds);
//        return query.getResultList();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        indexPets();
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
        if(user != null)  boolJunction.must(queryBuilder.keyword().onField("user.username").matching(user.getUsername()).createQuery());
        if(species != null) boolJunction.must(queryBuilder.phrase().onField("species.en_us").sentence(species.getEn_us()).createQuery());
        if(breed != null) boolJunction.must(queryBuilder.phrase().onField("breed.en_us").sentence(breed.getEn_us()).createQuery());
        if(gender != null)boolJunction.must(queryBuilder.keyword().onField("gender").matching(gender).createQuery());
        if(province != null)boolJunction.must(queryBuilder.phrase().onField("province.name").sentence(province.getName()).createQuery());
        if(department != null)boolJunction.must(queryBuilder.phrase().onField("department.name").sentence(department.getName()).createQuery());
        if(maxPrice != -1)boolJunction.must(queryBuilder.range().onField("price").below(maxPrice).createQuery());
        boolJunction.must(queryBuilder.range().onField("price").above(minPrice).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Pet.class);
        jpaQuery.setProjection("price");
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        List<Long> filteredIds = new ArrayList<>();
        for (Object[] id:results) {
            //filteredIds.add((Long)id[0]);
            System.out.println("wwwwwwwwww");
            System.out.println(id[0]);
        }
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Breed> query2 = em.createQuery("from Breed where id in :filteredIds", Breed.class);
        query2.setParameter("filteredIds", filteredIds);
        return query2.getResultList();
    }

    @Override
    public List<Department> searchDepartmentList(String locale, List<String> find, User user, Species species, Breed breed, String gender,
                                                 PetStatus status, int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery jpaQuery = searchIdsQuery(locale, find, user, species, breed, gender, status,
                minPrice,  maxPrice, province,  department);
        @SuppressWarnings("unchecked")
        List<Object[]> results = jpaQuery.getResultList();
        if (results.size() == 0) return new ArrayList<>();
        List<Long> filteredIds = new ArrayList<>();
        for (Object[] id:results) {
            filteredIds.add((Long)id[0]);
        }
        if (filteredIds.size() == 0) return new ArrayList<>();

        final TypedQuery<Department> query = em.createQuery("from Department where id in :filteredIds", Department.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    @Deprecated
    public List<Pet> filteredList(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                                  String searchCriteria, String searchOrder, int minPrice, int maxPrice, Province province,
                                  Department department, int page, int pageSize) {

        return searchList(locale, null, user, species, breed, gender, status, searchCriteria, searchOrder,
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
            return new ArrayList<Pet>();
        }
        final TypedQuery<Pet> query = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Async
    public void indexPets() {
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

    private org.hibernate.search.jpa.FullTextQuery searchIdsQuery(String locale, List<String> find, User user, Species species,
                                                                  Breed breed, String gender, PetStatus status, int minPrice,
                                                                  int maxPrice, Province province,
                                                                  Department department) {
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
        if(user != null)  boolJunction.must(queryBuilder.keyword().onField("user.username").matching(user.getUsername()).createQuery());
        if(species != null) boolJunction.must(queryBuilder.phrase().onField("species.en_us").sentence(species.getEn_us()).createQuery());
        if(breed != null) boolJunction.must(queryBuilder.phrase().onField("breed.en_us").sentence(breed.getEn_us()).createQuery());
        if(gender != null)boolJunction.must(queryBuilder.keyword().onField("gender").matching(gender).createQuery());
        if(province != null)boolJunction.must(queryBuilder.phrase().onField("province.name").sentence(province.getName()).createQuery());
        if(department != null)boolJunction.must(queryBuilder.phrase().onField("department.name").sentence(department.getName()).createQuery());
        if(maxPrice != -1)boolJunction.must(queryBuilder.range().onField("price").below(maxPrice).createQuery());
        boolJunction.must(queryBuilder.range().onField("price").above(minPrice).createQuery());

        org.apache.lucene.search.Query query = boolJunction.createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Pet.class);
        jpaQuery.setProjection(ProjectionConstants.ID);
        return jpaQuery;
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM pets where status = 0");
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public int getSearchListAmount(String locale, List<String> find, User user, Species species, Breed breed, String gender, PetStatus status,
                                   int minPrice, int maxPrice, Province province, Department department) {
        org.hibernate.search.jpa.FullTextQuery query = searchIdsQuery(locale, find, user, species, breed, gender, status,
                minPrice,  maxPrice, province,  department);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results.size();
    }

    @Override
    @Deprecated
    public int getFilteredListAmount(String locale, User user, Species species, Breed breed, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Province province, Department department) {
        return getSearchListAmount(locale, null, user, species, breed, gender, status, minPrice,  maxPrice, province, department);
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
        query.setParameter("old", oldStatus.getValue());
        query.setParameter("new", newStatus.getValue());
        query.setParameter("user", user.getId());
        query.executeUpdate();
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

        if(filteredIds.size() == 0){
            return new ArrayList<>();
        }
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
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        today = cal.getTime();
        Question question = new Question(content, user, target, pet, today, status);
        em.persist(question);
        return question;
    }

    @Override
    public Answer createAnswer(Question question, String content, User user, User target, Pet pet, QuestionStatus status) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        today = cal.getTime();
        Answer answer = new Answer(question, content, user, target, pet, today, status);
        em.persist(answer);
        return answer;
    }
}