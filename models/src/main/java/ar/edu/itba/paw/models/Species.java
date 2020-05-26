package ar.edu.itba.paw.models;

public class Species implements Comparable<Species>{

    private long id;
    private String name;

    public Species(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Species() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Species species = (Species) o;
        return id == species.id;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + name + " }";
    }

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

    @Override
    public int compareTo(Species o) {
        return name.compareTo(o.name);
    }
}
