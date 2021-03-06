package ar.edu.itba.paw.webapp.dto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.models.Pet;

public class PetDto {

    private Long id;
    private String petName;
    private LocalDateTime birthDate;
    private String gender;
    private boolean vaccinated;
    private Integer price;
    private String uploadDate;
    private String description;
    private Integer status;
    private URI species;
    private URI breed;
    private URI province;
    private URI department;
    private URI user;
    private URI newOwner;
    private URI questionList;
    private List<Long> images;
    private String username;

    private List<MultipartFile> photos;
    private Long userId;
    private Long newOwnerId;
    private Long speciesId;
    private Long breedId;
    private Long provinceId;
    private Long departmentId;

    /* Check if user has already requested pet */
    // private int lastRequestStatus;
    // private boolean requestExists;

    // private URI availableUsers;
    // private int availableAmount;


    public static PetDto fromPet(Pet pet, UriInfo uriInfo) {
        final PetDto dto = new PetDto();

        dto.id = pet.getId();
        dto.petName = pet.getPetName();
        dto.birthDate = pet.getBirthDate();
        dto.gender = pet.getGender();
        dto.vaccinated = pet.isVaccinated();
        dto.price = pet.getPrice();
        dto.uploadDate = pet.getUploadDate().toString();
        dto.description = pet.getDescription();
        dto.status = pet.getStatus().getValue();
        dto.images = new ArrayList<>();
        pet.getImages().forEach(i -> dto.images.add(i.getId()));
        dto.username = pet.getUser().getUsername();
        dto.breed = uriInfo.getBaseUriBuilder().path("species").path("breeds").path(String.valueOf(pet.getBreed().getId())).build();
        dto.species = uriInfo.getBaseUriBuilder().path("species").path(String.valueOf(pet.getSpecies().getId())).build();
        dto.province = uriInfo.getAbsolutePathBuilder().path("location").path("provinces").path(String.valueOf(pet.getProvince().getId())).build();
        dto.department = uriInfo.getAbsolutePathBuilder().path("location").path("departments").path(String.valueOf(pet.getDepartment().getId())).build();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getUser().getId())).build();
        if(pet.getNewOwner() != null) {
            dto.newOwner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getNewOwner().getId())).build();
            dto.newOwnerId = pet.getNewOwner().getId();
        }
        dto.questionList = uriInfo.getAbsolutePathBuilder().path("questions").build();

        dto.userId = pet.getUser().getId();
        dto.speciesId = pet.getSpecies().getId();
        dto.breedId = pet.getBreed().getId();
        dto.provinceId = pet.getProvince().getId();
        dto.departmentId = pet.getDepartment().getId();

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
        dto.uploadDate = pet.getUploadDate().toString();
        dto.description = pet.getDescription();
        dto.status = pet.getStatus().getValue();
        dto.images = new ArrayList<>();
        pet.getImages().forEach(i -> dto.images.add(i.getId()));
        dto.username = pet.getUser().getUsername();
        dto.breed = uriInfo.getBaseUriBuilder().path("species").path("breeds").path(String.valueOf(pet.getBreed().getId())).build();
        dto.species = uriInfo.getBaseUriBuilder().path("species").path(String.valueOf(pet.getSpecies().getId())).build();
        dto.province = uriInfo.getAbsolutePathBuilder().path("location").path("provinces").path(String.valueOf(pet.getProvince().getId())).build();
        dto.department = uriInfo.getAbsolutePathBuilder().path("location").path("departments").path(String.valueOf(pet.getDepartment().getId())).build();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getUser().getId())).build();
        if(pet.getNewOwner() != null) {
            dto.newOwner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(pet.getNewOwner().getId())).build();
            dto.newOwnerId = pet.getNewOwner().getId();
        }
        dto.questionList = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id)).path("questions").build();

        dto.userId = pet.getUser().getId();
        dto.speciesId = pet.getSpecies().getId();
        dto.breedId = pet.getBreed().getId();
        dto.provinceId = pet.getProvince().getId();
        dto.departmentId = pet.getDepartment().getId();

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

//    public LocalDateTime getUploadDate() {
//        return uploadDate;
//    }
//
//    public void setUploadDate(LocalDateTime uploadDate) {
//        this.uploadDate = uploadDate;
//    }


    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public List<Long> getImages() {
        return images;
    }

    public void setImages(List<Long> images) {
        this.images = images;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<MultipartFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MultipartFile> photos) {
        this.photos = photos;
    }

    public Long getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(Long newOwnerId) {
        this.newOwnerId = newOwnerId;
    }
}
