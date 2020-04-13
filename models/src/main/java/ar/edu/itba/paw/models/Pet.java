package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pet {
    private long id;
    private String petName;
    private Date birthDate;
    private String species;
    private String breed;
    private String gender;
    private boolean vaccinated;
    private Date uploadDate;
    private int price;
    private String location;
    private String description;
    private long ownerId;
    private List<Image> images;

    public Pet() {}

    public Pet(long id, String petName, String species, String breed, String location, boolean vaccinated, String gender, String description, Date birthDate, Date uploadDate, int price, long ownerId ) {
        this.id = id;
        this.petName = petName;
        this.birthDate = birthDate;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.uploadDate = uploadDate;
        this.price = price;
        this.location = location;
        this.description = description;
        this.ownerId = ownerId;
        this.images = new ArrayList<>();
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
