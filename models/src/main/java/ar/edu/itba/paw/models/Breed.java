package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity(name = "Breeds")
public class Breed implements Comparable<Breed>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "breeds_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "breeds_id_seq", name = "breeds_id_seq")
    private Long id;

    @Column(length = 255, nullable = false)
    private String en_us;

    @Column(length = 255, nullable = false)
    private String es_ar;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "speciesId")
    private Species species;

    protected Breed() {
        // Hibernate
    }

    public Breed(Species species, String en_us, String es_ar) {
        this.species = species;
        this.en_us = en_us;
        this.es_ar = es_ar;
    }

    @Deprecated
    public Breed(Long id, String name, Species species) {
        this.id = id;
        this.en_us = name;
        this.es_ar = name;
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Breed breed = (Breed) o;
        return id.equals(breed.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + en_us + ", species: " + species + " }";
    }

    public Long getId() {
        return id;
    }

    public Species getSpecies() {
        return species;
    }

    public String getName() {
        return en_us;
    }

    @Override
    public int compareTo(Breed o) {
        return getName().compareTo(o.getName());
    }
}
