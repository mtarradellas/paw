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
    private String location;
    private String description;
    private long ownerId;
    private List<Long> images;

    public Pet() {}

    public Pet(long id, String petName, Species species, Breed breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId ) {
        this.id = id;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.uploadDate = uploadDate;
        this.price = price;
        this.location = location;
        this.description = description;
        this.ownerId = ownerId;
        this.images = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<Long> getImages() {
        return images;
    }

    public void setImages(List<Long> images) {
        this.images = images;
    }
}
