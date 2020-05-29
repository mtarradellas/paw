package ar.edu.itba.paw.models;

import javax.persistence.*;

public class Status {

    private int id;

    private String en_us;

    private String es_ar;

    private String name;

    protected Status() {
        // Hibernate
    }

    public Status(String en_us, String es_ar) {
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.name = es_ar;
    }

    @Deprecated
    public Status(int id, String name) {
        this.id = id;
        this.en_us = name;
        this.es_ar = name;
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

    public String getName() {
        return name;
    }

    public void setName(String locale) {
        if (locale.equalsIgnoreCase("en_us")) {
            name = en_us;
        } else {
            name = es_ar;
        }
    }
}
