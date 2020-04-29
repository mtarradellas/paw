package ar.edu.itba.paw.webapp.form;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UploadPetForm {

    private byte[] photo;

    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String petName;

    private String speciesName;

    private String breedName;

    @Size(min = 3, max = 50)
    private String location;

    private boolean vaccinated;

    private String gender;

    @Size(max = 250)
    private String description;

    private String birthDate;

    @Pattern(regexp = "^[1-9][0-9]+$")
    private int price;

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
