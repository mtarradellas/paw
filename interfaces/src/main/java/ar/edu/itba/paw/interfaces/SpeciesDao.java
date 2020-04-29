package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;

import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesDao {
    Stream<Species> speciesList(String language);
    Stream<Breed> breedsList(String language);
    Optional<Species> findSpeciesByName(String language, String name);
    Optional<Breed> findBreedByName(String language, String name);
}
