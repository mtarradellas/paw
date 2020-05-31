package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Species implements Comparable<Species>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "species_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "species_id_seq", name = "species_id_seq")
    private Long id;

    @Column(nullable = false)
    private String en_us;

    @Column(nullable = false)
    private String es_ar;

    @OneToMany(orphanRemoval = true, mappedBy = "species", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Breed> breedList;

    private String name;

    protected Species() {
        // Hibernate
    }

    public Species(String en_us, String es_ar) {
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.name = es_ar;
    }

    @Deprecated
    public Species(Long id, String name) {
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
        Species species = (Species) o;
        return id.equals(species.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", en_us: " + en_us + " }";
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setLocale(String locale) {
        if (locale.equalsIgnoreCase("en_us")) {
            name = en_us;
        } else {
            name = es_ar;
        }
    }

    public List<Breed> getBreedList() {
        return breedList;
    }

    @Override
    public int compareTo(Species o) {
        return getName().compareTo(o.getName());
    }
}
