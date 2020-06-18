package ar.edu.itba.paw.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.builtin.LongBridge;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Species")
@Indexed
public class Species implements Comparable<Species>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "species_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "species_id_seq", name = "species_id_seq")
    @Field(name= "eid")
    @FieldBridge(impl = LongBridge.class)
    private Long id;

    @Column(nullable = false)
    @Field
    private String en_us;

    @Column(nullable = false)
    @Field
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getEn_us() {
        return en_us;
    }

    public void setEn_us(String en_us) {
        this.en_us = en_us;
    }

    public String getEs_ar() {
        return es_ar;
    }

    public void setEs_ar(String es_ar) {
        this.es_ar = es_ar;
    }

    public List<Breed> getBreedList() {
        return breedList;
    }

    public void setBreedList(List<Breed> breedList) {
        this.breedList = breedList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocale(String locale) {
        if (locale.equalsIgnoreCase("en_us")) {
            name = en_us;
        } else {
            name = es_ar;
        }
    }

    @Override
    public int compareTo(Species o) {
        return getName().compareTo(o.getName());
    }
}
