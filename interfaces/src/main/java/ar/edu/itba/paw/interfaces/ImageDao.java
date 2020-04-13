package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;

import java.util.Optional;

public interface ImageDao {
    Optional<Image> findById(Integer id);
}
