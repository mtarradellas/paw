package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesDao {
    List<Species> speciesList(int page, int pageSize);
    List<Breed> breedList(int page, int pageSize);
    Optional<Species> findSpeciesByName(String locale, String name);
    Optional<Breed> findBreedByName(String locale, String name);
    Optional<Species> findSpeciesById(long id);
    Optional<Breed> findBreedById(long id);
}
