package ar.edu.itba.paw.models;

import java.util.*;

public class PetList extends AbstractList<Pet> {

    private final Pet[] petList;
    private final SpeciesList speciesList;
    private final BreedList breedList;
    private final String maxPage;
    private final int totalPetsAmount;

    public PetList(List<Pet> petArray, String maxPage, int totalPetsAmount) {
        petList = petArray.toArray(new Pet[0]);
        this.maxPage = maxPage;
        this.speciesList = new SpeciesList(this);
        this.breedList = new BreedList(this);
        this.totalPetsAmount = totalPetsAmount;
    }

    public Pet get(int index) {
        return petList[index];
    }

    public Pet set(int index, Pet pet) {
        Pet oldValue = petList[index];
        petList[index] = pet;
        return oldValue;
    }

    public int size() {
        return petList.length;
    }

    public BreedList getBreeds() {
        return breedList;
    }

    public SpeciesList getSpecies() {
        return speciesList;
    }

    public String getMaxPage() {
        return maxPage;
    }

    public int getTotalPetsAmount() {
        return totalPetsAmount;
    }
}