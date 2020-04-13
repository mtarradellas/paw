package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> findById(Integer id);
}
