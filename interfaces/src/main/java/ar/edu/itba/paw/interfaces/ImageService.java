package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;


import java.util.Optional;
import java.util.stream.Stream;

public interface ImageService {
    Stream<Image> findByPetId(long id);
    Optional<byte[]> getDataById(long id);
}
