package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;


import java.util.stream.Stream;

public interface ImageDao {
    Stream<Image> findByPetId(long id);
}
