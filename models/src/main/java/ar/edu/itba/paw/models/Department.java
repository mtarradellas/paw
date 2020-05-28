package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name ="Departments")
public class Department implements Comparable<Department>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "departments_id_seq", name = "departments_id_seq")
    private Long id;

    @Column(length = 255, nullable = false)
    private String en_us;

    @Column(length = 255, nullable = false)
    private String es_ar;

    //private String name;
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "province")
    private Province province;

    protected Department() {
        //Hibernate
    }

    public Department(long id, String en_us, String es_ar, double latitude, double longitude, Province province) {
        this.id = id;
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
    }

    public Department(long id, String en_us, String es_ar, Province province) {
        this.id = id;
        this.en_us = en_us;
        this.es_ar = es_ar;
        this.province = province;
    }

    public Department(long id, String name, double latitude, double longitude) {
        this.id = id;
        this.en_us = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Department(long id, String name, double latitude, double longitude, Province province) {
        this.id = id;
        this.en_us = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + en_us + ", prov: " + province + " }";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Department o) {
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

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
