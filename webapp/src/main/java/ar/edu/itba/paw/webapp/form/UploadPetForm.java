package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;


public class UploadPetForm {

    @Size(min=1, max=5)
    @NotNull
    @FileSize(max=10)
    @NotEmptyMultipart
    @ImageDimensions
    private List<MultipartFile> photos;

    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Zñ]+$")
    private String petName;

    @SpeciesIdMatch
    private Long speciesId;

    @BreedIdMatch
    private Long breedId;

    @DepartmentIdMatch
    private Long department;

    @ProvinceIdMatch
    private Long province;

    @NotNull
    private Boolean vaccinated;

    @Pattern(regexp="^(male)|(female)$")
    private String gender;

    @Size(max = 512)
    private String description;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotNull
    @Min(value=0)
    private Integer price;

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

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public Long getBreedId() {
        return breedId;
    }

    public void setBreedId(Long breedId) {
        this.breedId = breedId;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
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
