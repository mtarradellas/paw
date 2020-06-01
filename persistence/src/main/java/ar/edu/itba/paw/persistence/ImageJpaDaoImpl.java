package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ImageJpaDaoImpl implements ImageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Image> findByPetId(Long id) {
        final String qStr = "from Images as i where i.petId = :petId";
        final TypedQuery<Image> query = em.createQuery(qStr, Image.class);
        query.setParameter("petId", id);
        return query.getResultList();
    }

    @Override
    public Integer quantityByPetId(Long id) {
        final String qStr = "select count(*) from Images as i where i.petId = :petId";
        final TypedQuery<Long> query = em.createQuery(qStr, Long.class);
        query.setParameter("petId", id);
        return query.getSingleResult().intValue();
    }

    @Override
    public Optional<byte[]> getDataById(Long id) {
        final String qStr = "from Images as i where i.id = :id";
        final TypedQuery<Image> query = em.createQuery(qStr, Image.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult().getImageData());
    }

    @Override
    public Optional<Image> create(Long petId, byte[] bytes) {
        Image image = new Image();
        image.setPetId(petId);
        image.setImageData(bytes);
        em.persist(image);
        return Optional.of(image);
    }

    @Override
    public void delete(Long id) {
        Query query = em.createQuery("delete Images where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id: ids ) {
            Query query = em.createQuery("delete Images where id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        }
    }
}
