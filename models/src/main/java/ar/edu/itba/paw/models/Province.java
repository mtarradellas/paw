package ar.edu.itba.paw.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Provinces")
@Indexed
public class Province implements Comparable<Province>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provinces_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "provinces_id_seq", name = "provinces_id_seq")
    private Long id;


    @Column(length = 255, nullable = false)
    @Field
    private String name;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @OneToMany(orphanRemoval = true, mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Department> departmentList;

    protected Province(){
        //Hibernate
    }

    public Province(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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
        return "{ id: " + id + ", name: " + name+ " }";
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
