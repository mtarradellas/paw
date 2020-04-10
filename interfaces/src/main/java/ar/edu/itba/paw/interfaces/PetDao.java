package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.Optional;
import java.util.stream.Stream;

public interface PetDao {
    Optional<Pet> findById(long id);
    Stream<Pet> list();
    Optional<Pet> save(Pet pet);
}
