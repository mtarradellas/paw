package ar.edu.itba.paw.models;

import java.util.Date;

public class Pet {
    private String id;
    private String name;
    private Date birthDate;
    private String species;
    private String breed;
    private String gender;
    private boolean vaccinated;
    private Date uploadDate;
    private int price;
    private String location;
    //list of photos?
    // Owner?


    public Pet() {}

    public Pet(String id, String name, Date birthDate, String species, String breed, String gender, boolean vaccinated, Date uploadDate, int price, String location) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.vaccinated = vaccinated;
        this.uploadDate = uploadDate;
        this.price = price;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
