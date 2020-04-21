package ar.edu.itba.paw.models;

public class Species {

    private long id;
    private String name;

    public Species(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Species() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
