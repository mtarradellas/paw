package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pet;

import java.util.Optional;
import java.util.stream.Stream;

public interface PetService {
    Optional<Pet> findById(long id);
    Stream<Pet> list();
    Optional<Pet> save(Pet pet);


    //List<Pet> search(String keyWords);
    //List<Pet> filter(); como se pasarian los filtros?
}
