package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.BreedList;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.SpeciesList;

import java.util.Optional;
import java.util.stream.Stream;

public interface SpeciesService {
    SpeciesList speciesList(String locale);
    BreedList breedsList(String locale);
}
