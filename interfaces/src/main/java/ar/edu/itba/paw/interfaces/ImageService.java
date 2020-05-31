package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> findByPetId(long id);
    Integer quantityByPetId(long id);
    Optional<byte[]> getDataById(long id);
    Optional<Image> create(long petId, byte[] bytes, long userId);
    void delete(Long id);
    void delete(List<Long> ids);
}
