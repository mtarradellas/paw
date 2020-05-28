package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
@Entity(name = "Provinces")
public class Province implements Comparable<Province>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provinces_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "provinces_id_seq", name = "provinces_id_seq")
    private Long id;

    @Column(length = 255, nullable = false)
    private String en_us;

    @Column(length = 255, nullable = false)
    private String es_ar;

    //private String name;
    @Column
    private double latitude;

    @Column
    private double longitude;

    protected Province(){
        //Hibernate
    }

    public Province(long id, String en_us, String es_ar, double latitude, double longitude) {
        this.id = id;
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Province(long id, String name, double latitude, double longitude) {
        this.id = id;
        this.en_us = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Province(long id, String en_us, String es_ar) {
        this.id = id;
        this.en_us = en_us;
        this.es_ar = es_ar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return id.equals(province.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + en_us + " }";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Province o) {
        return getName().compareTo(o.getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return en_us;
    }

    public void setName(String name) {
        this.en_us = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
