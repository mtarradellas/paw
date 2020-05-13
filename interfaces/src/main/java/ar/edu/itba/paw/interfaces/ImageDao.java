package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ImageDao {
    Stream<Image> findByPetId(long id);
    Integer quantityByPetId(long id);
    Optional<byte[]> getDataById(long id);
    Optional<Image> create(long petId, byte[] bytes);
    void delete(Integer id);
    void delete(List<Integer> ids);
}
