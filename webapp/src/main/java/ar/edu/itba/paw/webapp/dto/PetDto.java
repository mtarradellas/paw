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
    private URI species;
    private URI breed;
    private URI province;
    private URI department;
    private URI user;
    private URI newOwner;
    private URI questionList;
    private URI images;
    /* Check if user has already requested pet */
    private int lastRequestStatus;
    private boolean requestExists;

    private URI availableUsers;
    private int availableAmount;


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
        dto.breed = uriInfo.getBaseUriBuilder().path("species").path("breeds").path(String.valueOf(pet.getBreed().getId())).build();
        dto.species = uriInfo.getBaseUriBuilder().path("species").path(String.valueOf(pet.getSpecies().getId())).build();
        dto.province = uriInfo.getAbsolutePathBuilder().path("location").path("provinces").path(String.valueOf(pet.getProvince().getId())).build();
        dto.department = uriInfo.getAbsolutePathBuilder().path("location").path("departments").path(String.valueOf(pet.getDepartment().getId())).build();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getUser().getId())).build();
        if(pet.getNewOwner() != null) dto.newOwner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getNewOwner().getId())).build();
        dto.questionList = uriInfo.getAbsolutePathBuilder().path("questions").build();
        dto.images = uriInfo.getAbsolutePathBuilder().path("images").build();
// si el usuario es el duenio del pet, darle la lista de los usuarios a los que puede asignarle el pet
//        if (pet.getUser().getId().equals(user.getId())) {
//            dto.availableUsers = user.getInterestList().stream()
//                    .filter(r -> (r.getStatus() == RequestStatus.ACCEPTED) && r.getPet().getId().equals(pet.getId()))
//                    .map(Request::getUser).collect(Collectors.toList());
//            dto.availableAmount = dto.availableUsers.size();
//        }

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
        dto.breed = uriInfo.getBaseUriBuilder().path("species").path("breeds").path(String.valueOf(pet.getBreed().getId())).build();
        dto.species = uriInfo.getBaseUriBuilder().path("species").path(String.valueOf(pet.getSpecies().getId())).build();
        dto.province = uriInfo.getAbsolutePathBuilder().path("location").path("provinces").path(String.valueOf(pet.getProvince().getId())).build();
        dto.department = uriInfo.getAbsolutePathBuilder().path("location").path("departments").path(String.valueOf(pet.getDepartment().getId())).build();

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

    public URI getSpecies() {
        return species;
    }

    public void setSpecies(URI species) {
        this.species = species;
    }

    public URI getBreed() {
        return breed;
    }

    public void setBreed(URI breed) {
        this.breed = breed;
    }

    public URI getProvince() {
        return province;
    }

    public void setProvince(URI province) {
        this.province = province;
    }

    public URI getDepartment() {
        return department;
    }

    public void setDepartment(URI department) {
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
