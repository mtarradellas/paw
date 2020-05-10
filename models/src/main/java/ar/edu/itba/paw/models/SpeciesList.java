package ar.edu.itba.paw.models;

import java.util.*;

public class SpeciesList extends AbstractList<Species> {

    private final Species[] speciesList;

    public SpeciesList(List<Species> speciesArray) {
        super();
        speciesList = speciesArray.toArray(new Species[0]);
    }

    public SpeciesList(BreedList breedList) {
        super();
        TreeSet<Species> speciesSet = new TreeSet<>();
        breedList.forEach(breed -> {
            speciesSet.add(breed.getSpecies());
        });
        speciesList = speciesSet.toArray(new Species[0]);
    }

    public SpeciesList(PetList petList) {
        super();
        TreeSet<Species> speciesSet = new TreeSet<>();
        petList.forEach(pet -> {
            speciesSet.add(pet.getSpecies());
        });
        speciesList = speciesSet.toArray(new Species[0]);
    }

    public Species get(int index) {
        return speciesList[index];
    }

    public Species set(int index, Species species) {
        Species oldValue = speciesList[index];
        speciesList[index] = species;
        return oldValue;
    }

    public int size() {
        return speciesList.length;
    }
}
