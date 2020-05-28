package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.BreedList;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.SpeciesList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesService {
    SpeciesList speciesList(String locale);
    BreedList breedList(String locale);
    SpeciesList speciesList();
    BreedList breedList();
    Optional<Species> findSpeciesByName(String locale, String name);
    Optional<Breed> findBreedByName(String locale, String name);
    Optional<Species> findSpeciesById(String locale, long id);
    Optional<Breed> findBreedById(String locale, long id);
}
