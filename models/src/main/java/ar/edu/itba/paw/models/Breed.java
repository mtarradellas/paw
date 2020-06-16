package ar.edu.itba.paw.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;

@Entity
@Table(name = "Breeds")
@Indexed
public class Breed implements Comparable<Breed>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "breeds_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "breeds_id_seq", name = "breeds_id_seq")
    private Long id;

    @Column(nullable = false)
    @Field(store = Store.YES)
    private String en_us;

    @Column(nullable = false)
    @Field
    private String es_ar;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "speciesId")
    private Species species;

    private String name;

    protected Breed() {
        // Hibernate
    }

    public Breed(Species species, String en_us, String es_ar) {
        this.species = species;
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.name = es_ar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
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

    public String getEn_us() {
        return en_us;
    }

    public String getEs_ar() {
        return es_ar;
    }

    public Species getSpecies() {
        return species;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEn_us(String en_us) {
        this.en_us = en_us;
    }

    public void setEs_ar(String es_ar) {
        this.es_ar = es_ar;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setLocale(String locale) {
        if (locale.equalsIgnoreCase("en_us")) {
            name = en_us;
        } else {
            name = es_ar;
        }
    }

    @Override
    public int compareTo(Breed o) {
        return getName().compareTo(o.getName());
    }
}
