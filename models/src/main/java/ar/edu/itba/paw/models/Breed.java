package ar.edu.itba.paw.models;

public class Breed {
    private long id;
    private long speciesId;
    private String name;


    public Breed(long id, long speciesId, String name) {
        this.id = id;
        this.speciesId = speciesId;
        this.name = name;
    }

    public Breed() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(long speciesId) {
        this.speciesId = speciesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}