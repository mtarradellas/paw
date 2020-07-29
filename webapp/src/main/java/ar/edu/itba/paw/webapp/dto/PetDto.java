package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class PetDto {

    private Long id;
    private String petName;
    private LocalDateTime birthDate;
    private String gender;
    private boolean vaccinated;
    private int price;
    private LocalDateTime uploadDate;
    private String description;
    private PetStatus status;
    private SpeciesDto species;
    private BreedDto breed;
    private ProvinceDto province;
    private DepartmentDto department;
    private URI user;
    private URI newOwner;
    private URI questionList;
    private URI images;

    public static PetDto fromPet(Pet pet, UriInfo uriInfo) {
        final PetDto dto = new PetDto();

        dto.id = pet.getId();
        dto.petName = pet.getPetName();
        dto.birthDate = pet.getBirthDate();
        dto.gender = pet.getGender();
        dto.vaccinated = pet.isVaccinated();
        dto.price = pet.getPrice();
        dto.uploadDate = pet.getUploadDate();
        dto.description = pet.getDescription();
        dto.status = pet.getStatus();
        dto.breed = BreedDto.fromBreed(pet.getBreed());
        dto.species = SpeciesDto.fromSpecies(pet.getSpecies(), uriInfo);
        dto.province = ProvinceDto.fromProvince(pet.getProvince());
        dto.department = DepartmentDto.fromDepartment(pet.getDepartment());

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getUser().getId())).build();
        if(pet.getNewOwner() != null) dto.newOwner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getNewOwner().getId())).build();
        dto.questionList = uriInfo.getAbsolutePathBuilder().path("questions").build();
        dto.images = uriInfo.getAbsolutePathBuilder().path("images").build();

        return dto;
    }

    public static PetDto fromPetForList(Pet pet, UriInfo uriInfo) {
        final PetDto dto = new PetDto();

        dto.id = pet.getId();
        dto.petName = pet.getPetName();
        dto.birthDate = pet.getBirthDate();
        dto.gender = pet.getGender();
        dto.vaccinated = pet.isVaccinated();
        dto.price = pet.getPrice();
        dto.uploadDate = pet.getUploadDate();
        dto.description = pet.getDescription();
        dto.status = pet.getStatus();
        dto.breed = BreedDto.fromBreed(pet.getBreed());
        dto.species = SpeciesDto.fromSpecies(pet.getSpecies(),uriInfo);
        dto.province = ProvinceDto.fromProvince(pet.getProvince());
        dto.department = DepartmentDto.fromDepartment(pet.getDepartment());

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getUser().getId())).build();
        if(pet.getNewOwner() != null) dto.newOwner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getNewOwner().getId())).build();
        dto.questionList = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id)).path("questions").build();
        dto.images = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id)).path("images").build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }

    public SpeciesDto getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesDto species) {
        this.species = species;
    }

    public BreedDto getBreed() {
        return breed;
    }

    public void setBreed(BreedDto breed) {
        this.breed = breed;
    }

    public ProvinceDto getProvince() {
        return province;
    }

    public void setProvince(ProvinceDto province) {
        this.province = province;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(URI newOwner) {
        this.newOwner = newOwner;
    }

    public URI getQuestionList() {
        return questionList;
    }

    public void setQuestionList(URI questionList) {
        this.questionList = questionList;
    }

    public URI getImages() {
        return images;
    }

    public void setImages(URI images) {
        this.images = images;
    }
}
