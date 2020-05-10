package ar.edu.itba.paw.models;

public class Breed implements Comparable<Breed>{
    private long id;
    private Species species;
    private String name;


    public Breed(long id, String name, Species species) {
        this.id = id;
        this.name = name;
        this.species = species;
    }

    public Breed() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Breed breed = (Breed) o;
        return id == breed.id;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + name + ", species: " + species + " }";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Breed o) {
        return name.compareTo(o.name);
    }
}
