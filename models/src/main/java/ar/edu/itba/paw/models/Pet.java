package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.PetStatus;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.hibernate.search.bridge.builtin.LongBridge;
import org.hibernate.search.bridge.builtin.StringBridge;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Pets")
@Indexed
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pets_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "pets_id_seq", name = "pets_id_seq")
    @Field(name= "eid")
    @FieldBridge(impl = LongBridge.class)
    private Long id;

    @Column(nullable = false)
    @Field
    private String petName;

    @Column
    private Date birthDate;

    @Column(length = 16, nullable = false)
    @Field(store = Store.YES)
    private String gender;

    @Column(nullable = false)
    private boolean vaccinated;

    @Field(store = Store.YES)
    @NumericField
    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Date uploadDate;

    @Column(length = 4086)
    private String description;

    @Field
    @NumericField
    private int status;

    @ContainedIn
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    @IndexedEmbedded(depth = 2)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soldto")
    private User newOwner;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "species")
    @IndexedEmbedded(depth = 1)
    private Species species;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "breed")
    @IndexedEmbedded(depth = 1)
    private Breed breed;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "province")
    @IndexedEmbedded(depth = 1)
    private Province province;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    @IndexedEmbedded(depth = 1)
    private Department department;

    @OneToMany(orphanRemoval = true, mappedBy = "petId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImageDTO> images;

    public Pet() {
        // Hibernate
    }

    public Pet(String petName, Date birthDate, String gender, boolean vaccinated, int price, Date uploadDate,
               String description, PetStatus status, User user, Species species, Breed breed, Province province, Department department) {

        this.petName = petName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.price = price;
        this.uploadDate = uploadDate;
        this.description = description;
        this.status = status.getValue();
        this.user = user;
        this.species = species;
        this.breed = breed;
        this.province = province;
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id.equals(pet.id) &&
                user.equals(pet.user);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id.intValue();
        hash = 31 * hash + user.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + petName + ", species: " + species + ", breed: " + breed +
                ", owner: " + user + ", status: " + status + ", dept: " + department + ", prov: " + province + " }";
    }

    public void setLocale(String locale) {
        this.breed.setLocale(locale);
        this.species.setLocale(locale);
    }

    public Long getId() {
        return id;
    }

    public String getPetName() {
        return petName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public int getPrice() {
        return price;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public PetStatus getStatus() {
        return PetStatus.values()[status];
    }

    public User getUser() {
        return user;
    }

    public User getNewOwner() {
        return newOwner;
    }

    public Species getSpecies() {
        return species;
    }

    public Breed getBreed() {
        return breed;
    }

    public Province getProvince() {
        return province;
    }

    public Department getDepartment() {
        return department;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(PetStatus status) {
        this.status = status.getValue();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNewOwner(User newOwner) {
        this.newOwner = newOwner;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
