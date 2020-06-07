package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import java.util.List;
import java.util.Optional;

public interface SpeciesService {
    List<Species> speciesList(String locale);
    List<Breed> breedList(String locale);
    List<Species> speciesList();
    List<Breed> breedList();

    Optional<Species> findSpeciesByName(String locale, String name);
    Optional<Breed> findBreedByName(String locale, String name);
    Optional<Species> findSpeciesById(String locale, long id);
    Optional<Breed> findBreedById(String locale, long id);
    Optional<Species> findSpeciesById(long id);
    Optional<Breed> findBreedById(long id);

    void setSpeciesLocale(String locale, Species species);
    void setBreedLocale(String locale, Breed breed);
    void setSpeciesLocale(String locale, List<Species> speciesList);
    void setBreedLocale(String locale, List<Breed> breedList);
}
