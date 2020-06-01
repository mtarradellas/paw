package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PetDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        final TypedQuery<Pet> query = em.createQuery("from Pet where id in :filteredIds", Pet.class);
        query.setParameter("filteredIds", filteredIds);
        List<Pet> petList = query.getResultList();
        return petList;
    }

    @Override
    public List<Pet> searchList(String find, int page, int pageSize) {
        return list(page, pageSize);
    }

    @Override
    public List<Pet> filteredList(User user, Species species, Breed breed, String gender, PetStatus status,
                                  String searchCriteria, String searchOrder, int minPrice, int maxPrice, Province province,
                                  Department department, int page, int pageSize) {
        return list(page, pageSize);
    }

    @Override
    public int getListAmount() {
        Query nativeQuery = em.createNativeQuery("SELECT count(*) FROM pets where status = 0");
        Number count = (Number) nativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    public int getSearchListAmount(String find) {
        return getListAmount();
    }

    @Override
    public int getFilteredListAmount(User user, Species species, Breed breed, String gender, PetStatus status,
                                     int minPrice, int maxPrice, Province province, Department department) {
        return getListAmount();
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