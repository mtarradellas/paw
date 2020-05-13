package ar.edu.itba.paw.models;

import java.util.AbstractList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

public class BreedList extends AbstractList<Breed> {

    private final Breed[] breedList;
    private final SpeciesList speciesList;

    public BreedList(List<Breed> breedArray) {
        super();
        breedList = breedArray.toArray(new Breed[0]);
        speciesList = new SpeciesList(this);
    }

    public BreedList(PetList petList) {
        super();
        TreeSet<Breed> breedSet = new TreeSet<>();
        petList.forEach(pet -> {
            breedSet.add(pet.getBreed());
        });
        breedList = breedSet.toArray(new Breed[0]);
        speciesList = new SpeciesList(petList);
    }

    public Breed get(int index) {
        return breedList[index];
    }

    public Breed set(int index, Breed breed) {
        Breed oldValue = breedList[index];
        breedList[index] = breed;
        return oldValue;
    }

    public int size() {
        return breedList.length;
    }

    public SpeciesList getSpecies() {
        return speciesList;
    }
}
