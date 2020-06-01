package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> findByPetId(Long id);
    Integer quantityByPetId(Long id);
    Optional<byte[]> getDataById(Long id);
    Optional<Image> create(Long petId, byte[] bytes, Long userId);
    void delete(Long id);
    void delete(List<Long> ids);
}
