package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.BreedIdMatch;
import ar.edu.itba.paw.webapp.validators.FileSize;
import ar.edu.itba.paw.webapp.validators.ImageDimensions;
import ar.edu.itba.paw.webapp.validators.SpeciesIdMatch;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

import java.util.Date;
import java.util.List;

public class EditPetForm {

    private List<Integer> imagesIdToDelete;

    @NotNull
    @FileSize
    @ImageDimensions
    private List<MultipartFile> photos;

    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String petName;

    @SpeciesIdMatch
    private Integer speciesId;

    @BreedIdMatch
    private Integer breedId;

    @Size(min = 3, max = 50)
    private String location;

    @NotNull
    private Boolean vaccinated;

    @Pattern(regexp="^(male)|(female)$")
    private String gender;

    @Size(max = 250)
    private String description;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotNull
    @Min(value=0)
    private Integer price;

    public List<Integer> getImagesIdToDelete() {
        return imagesIdToDelete;
    }

    public void setImagesIdToDelete(List<Integer> imagesIdToDelete) {
        this.imagesIdToDelete = imagesIdToDelete;
    }

    public List<MultipartFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MultipartFile> photos) {
        this.photos = photos;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    public Integer getBreedId() {
        return breedId;
    }

    public void setBreedId(Integer breedId) {
        this.breedId = breedId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Boolean vaccinated) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
