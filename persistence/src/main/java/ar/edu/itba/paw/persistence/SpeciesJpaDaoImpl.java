package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SpeciesJpaDaoImpl implements SpeciesDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Species> speciesList(int page, int pageSize) {
    Query nativeQuery = em.createNativeQuery("SELECT id FROM species");
    nativeQuery.setFirstResult((page - 1) * pageSize);
    nativeQuery.setMaxResults(pageSize);
    @SuppressWarnings("unchecked")
    List<? extends Number> resultList = nativeQuery.getResultList();
    List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

    final TypedQuery<Species> query = em.createQuery("from Species where id IN :filteredIds", Species.class);
    query.setParameter("filteredIds", filteredIds);
    return query.getResultList();
    }

    @Override
    public List<Breed> breedsList(int page, int pageSize) {
        Query nativeQuery = em.createNativeQuery("SELECT id FROM breeds");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<? extends Number> resultList = nativeQuery.getResultList();
        List<Long> filteredIds = resultList.stream().map(Number::longValue).collect(Collectors.toList());

        final TypedQuery<Breed> query = em.createQuery("from Breeds where id IN :filteredIds", Breed.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public Optional<Species> findSpeciesByName(String locale, String species) {
        final String vLocale = validateLocale(locale);
        final String qStr = "from Species as s where s." + vLocale + " = :species";
        final TypedQuery<Species> query = em.createQuery(qStr, Species.class);
        query.setParameter("species", species);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Breed> findBreedByName(String locale, String breed) {
        final String vLocale = validateLocale(locale);
        final String qStr = "from Breed as b where b." + vLocale + " = :breed";
        final TypedQuery<Breed> query = em.createQuery(qStr, Breed.class);
        query.setParameter("breed", breed);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Species> findSpeciesById(long id) {
        return Optional.ofNullable(em.find(Species.class, id));
    }

    @Override
    public Optional<Breed> findBreedById(long id) {
        return Optional.ofNullable(em.find(Breed.class, id));
    }

    private String validateLocale(String locale) {
        if (locale.equals("en_us")) return "en_us";
        return "es_ar";
    }
}


