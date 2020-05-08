package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;

import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesService {
    Stream<Species> speciesList(String language);
    Stream<Breed> breedsList(String language);
}
