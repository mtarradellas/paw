package ar.edu.itba.paw.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.LongBridge;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Departments")
@Indexed
public class Department implements Comparable<Department>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "departments_id_seq", name = "departments_id_seq")
    @Field(name = "eid", store = Store.YES)
    @FieldBridge(impl = LongBridge.class)
    private Long id;

    @Column(length = 255, nullable = false)
    @Field
    private String name;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "province", referencedColumnName="name")
    private Province province;

    protected Department() {
        //Hibernate
    }

    public Department(String name, double latitude, double longitude, Province province) {
        this.name = name;
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
        return "{ id: " + id + ", name: " + name + ", prov: " + province + " }";
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

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
