package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesDao {
    List<Species> speciesList(String locale);
    List<Breed> breedsList(String locale);
    Optional<Species> findSpeciesByName(String locale, String name);
    Optional<Breed> findBreedByName(String locale, String name);
    Optional<Species> findSpeciesById(String locale, long id);
    Optional<Breed> findBreedById(String locale, long id);
}
