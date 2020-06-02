package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Pet {
    private long id;
    private String petName;
    private Date birthDate;
    private Species species;
    private Breed breed;
    private String gender;
    private boolean vaccinated;
    private Date uploadDate;
    private int price;
    private String description;
    private long ownerId;
    private Status status;
    private List<Long> images;
    private Province province;
    private Department department;

    public Pet() {}

    public Pet(long id, String petName, Species species, Breed breed, boolean vaccinated, String gender,
               String description, Date birthDate, Date uploadDate, int price, long ownerId, Status status, Province province, Department department) {
        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.uploadDate = uploadDate;
        this.price = price;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
        this.images = new ArrayList<>();
        this.province = province;
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id
                && ownerId == pet.ownerId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) id;
        hash = 31 * hash + (int) ownerId;
        return hash;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: " + petName + ", species: " + species + ", breed: " + breed +
                ", owner: " + ownerId + ", status: " + status + ", dept: " + department + ", prov: " + province + " }";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public List<Long> getImages() {
        return images;
    }

    public void setImages(List<Long> images) {
        this.images = images;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
