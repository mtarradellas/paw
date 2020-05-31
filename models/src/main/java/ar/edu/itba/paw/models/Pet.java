package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.PetStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "Pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pets_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "pets_id_seq", name = "pets_id_seq")
    private Long id;

    @Column(nullable = false)
    private String petName;

    @Column
    private Date birthDate;

    @Column(length = 16, nullable = false)
    private String gender;

    @Column(nullable = false)
    private boolean vaccinated;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Date uploadDate;

    @Column(length = 4086)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private PetStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "species")
    private Species species;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "breed")
    private Breed breed;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "province")
    private Province province;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
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
        this.status = status;
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
        if (this.getClass() != o.getClass()) return false;
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
        return status;
    }

    public User getUser() {
        return user;
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
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
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
