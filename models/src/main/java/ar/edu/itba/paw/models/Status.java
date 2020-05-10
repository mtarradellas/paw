package ar.edu.itba.paw.models;

public class Status {
    private int id;
    private String name;

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return id == status.id;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + name + " }";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
