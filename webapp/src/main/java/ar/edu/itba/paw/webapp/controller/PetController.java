package ar.edu.itba.paw.webapp.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.InvalidImageQuantityException;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.PetException;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Department;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.dto.BreedDto;
import ar.edu.itba.paw.webapp.dto.DepartmentDto;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.dto.ProvinceDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@Path("/pets")
public class PetController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    private static final int PET_PAGE_SIZE = 12;

    @Autowired
    private PetService petService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPets(@Context HttpServletRequest httpRequest,
                            @QueryParam("ownerId") @DefaultValue("0") Long ownerId,
                            @QueryParam("newOwnerId") @DefaultValue("0") Long newOwnerId,
                            @QueryParam("species") @DefaultValue("0") Long species,
                            @QueryParam("breed") @DefaultValue("0") Long breed,
                            @QueryParam("province") @DefaultValue("0") Long province,
                            @QueryParam("department") @DefaultValue("0") Long department,
                            @QueryParam("status") @DefaultValue("-1") int status,
                            @QueryParam("gender") String gender,
                            @QueryParam("searchCriteria") String searchCriteria,
                            @QueryParam("find") String find,
                            @QueryParam("searchOrder") String searchOrder,
                            @QueryParam("priceRange") @DefaultValue("0") int priceRange,
                            @QueryParam("page") @DefaultValue("1") int page) {


        final String locale = ApiUtils.getLocale(httpRequest);
        int[] range;
        PetStatus petStatus = null;
        try {
            ownerId = ParseUtils.parseUserId(ownerId);
            newOwnerId = ParseUtils.parseUserId(newOwnerId);
            ParseUtils.parsePage(page);
            ParseUtils.isAllowedFind(find);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
            species = ParseUtils.parseSpecies(species);
            breed = ParseUtils.parseBreed(breed);
            province = ParseUtils.parseProvince(province);
            department = ParseUtils.parseDepartment(department);
            petStatus = ParseUtils.parseStatus(PetStatus.class, status);
            gender = ParseUtils.parseGender(gender);
            range = ParseUtils.parseRange(priceRange);
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        int minPrice = range[0];
        int maxPrice = range[1];
        List<String> findList = ParseUtils.parseFind(find);
        List<PetDto> petList;
        int amount;
        try {
            petList = petService.filteredList(locale, findList, ownerId, newOwnerId, species, breed, gender, petStatus,
                                        searchCriteria, searchOrder, minPrice, maxPrice, province, department, page, PET_PAGE_SIZE)
                                        .stream().map(p -> PetDto.fromPetForList(p, uriInfo)).collect(Collectors.toList());
            amount = petService.getFilteredListAmount(locale, findList, ownerId, newOwnerId, species, breed, gender, petStatus, minPrice,
                                        maxPrice, province, department);
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return ApiUtils.paginatedListResponse(amount, PET_PAGE_SIZE, page, uriInfo, petList, null);
    }

    @GET
    @Path("/locale")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response locale(@Context HttpServletRequest request) {
        String locale = ApiUtils.getLocale(request);
        Map<String, String> json = new HashMap<>();
        json.put("locale", locale);
        System.out.println("\n\n" + locale + "\n\n");

        return Response.ok(new Gson().toJson(json)).build();

    }

    @GET
    @Path("/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getFilters(@Context HttpServletRequest httpRequest,
                            @QueryParam("ownerId") @DefaultValue("0") Long ownerId,
                            @QueryParam("species") @DefaultValue("0") Long species,
                            @QueryParam("breed") @DefaultValue("0") Long breed,
                            @QueryParam("province") @DefaultValue("0") Long province,
                            @QueryParam("department") @DefaultValue("0") Long department,
                            @QueryParam("gender") String gender,
                            @QueryParam("find") String find,
                            @QueryParam("priceRange") @DefaultValue("0") int priceRange) {

        final String locale = ApiUtils.getLocale(httpRequest);
        int[] range;
        Long owner;
        try {
            owner = ParseUtils.parseUserId(ownerId);
            species = ParseUtils.parseSpecies(species);
            breed = ParseUtils.parseBreed(breed);
            province = ParseUtils.parseProvince(province);
            department = ParseUtils.parseDepartment(department);
            gender = ParseUtils.parseGender(gender);
            range = ParseUtils.parseRange(priceRange);
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        int minPrice = range[0];
        int maxPrice = range[1];
        List<String> findList = ParseUtils.parseFind(find);
        Set<String> genderList;
        Set<Integer> rangeList;
        List<Department> departments;
        List<Breed> breeds;
        try {
            breeds = petService.filteredBreedList(locale, findList, owner, species, breed, gender, PetStatus.AVAILABLE,
                 minPrice, maxPrice, province, department);
            departments = petService.filteredDepartmentList(locale, findList, owner, species, breed, gender, PetStatus.AVAILABLE,
                    minPrice, maxPrice, province, department);
            rangeList = petService.filteredRangesList(locale, findList, owner, species, breed, gender, PetStatus.AVAILABLE,
                    minPrice, maxPrice, province, department);
            genderList = petService.filteredGenderList(locale, findList, owner, species, breed, gender, PetStatus.AVAILABLE,
                    minPrice, maxPrice, province, department);
        } catch(NotFoundException | PetException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        List<SpeciesDto> speciesList = breeds.stream().map(Breed::getSpecies).distinct().sorted(Species::compareTo)
                .map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
        List<ProvinceDto> provinceList = departments.stream().map(Department::getProvince).distinct().sorted(Province::compareTo)
                .map(ProvinceDto::fromProvince).collect(Collectors.toList());
        List<DepartmentDto> departmentList = departments.stream().map(d -> DepartmentDto.fromDepartment(d,uriInfo)).collect(Collectors.toList());
        List<BreedDto> breedList = breeds.stream().map(BreedDto::fromBreed).collect(Collectors.toList());

        Map<String, Object> filters = new TreeMap<>();
        filters.put("speciesList", speciesList);
        filters.put("breedList", breedList);
        filters.put("departmentList", departmentList);
        filters.put("provinceList", provinceList);
        filters.put("genderList", genderList);
        filters.put("rangeList", rangeList);

        return Response.ok().entity(new Gson().toJson(filters)).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response create(@Context HttpServletRequest httpRequest, final PetDto pet) {
        String locale = ApiUtils.getLocale(httpRequest);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);
        if(loggedUser == null) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        try {
            ParseUtils.parsePet(pet);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<Pet> opNewPet;
        List<byte[]> photos = new ArrayList<>();
        if(pet.getPhotos().isEmpty()){
            LOGGER.warn("Photos is empty");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        try {
            for (MultipartFile photo : pet.getPhotos()) {
                if(!photo.isEmpty()) {
                    try {
                        photos.add(photo.getBytes());
                    } catch (IOException ex) {
                        LOGGER.warn(ex.getMessage());
                        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
                    }
                }
            }
        } catch (ImageLoadException ex) {
            final ErrorDto body = new ErrorDto(1, "Failed to load bytes from image");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        try {
            opNewPet = petService.create(locale, pet.getPetName(), pet.getBirthDate(), pet.getGender(), pet.isVaccinated(),
                    pet.getPrice(), pet.getDescription(), PetStatus.AVAILABLE, loggedUser.getId(), pet.getSpeciesId(), pet.getBreedId(),
                    pet.getProvinceId(), pet.getDepartmentId(), photos);
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if (!opNewPet.isPresent()) {
            LOGGER.warn("Pet creation failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI petUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(opNewPet.get().getId())).build();
        return Response.created(petUri).build();
    }

    @POST
    @Path("/{petId}/edit")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response edit(@Context HttpServletRequest httpRequest, final PetDto pet) {
        try {
            ParseUtils.parsePet(pet);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        String locale = ApiUtils.getLocale(httpRequest);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);

        List<byte[]> photos = new ArrayList<>();
        try {
            for (MultipartFile photo : pet.getPhotos()) {
                if(!photo.isEmpty()) {
                    try {
                        photos.add(photo.getBytes());
                    } catch (IOException ex) {
                        LOGGER.warn(ex.getMessage());
                        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
                    }
                }
            }
        } catch (ImageLoadException ex) {
            final ErrorDto body = new ErrorDto(1, "Failed to load bytes from image");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
                                
        Optional<Pet> opPet;
        try {
            opPet = petService.update(locale, pet.getId(), loggedUser.getId(), pet.getPetName(), pet.getBirthDate(), pet.getGender(),
                    pet.isVaccinated(), pet.getPrice(), pet.getDescription(), PetStatus.AVAILABLE, pet.getSpeciesId(),
                    pet.getBreedId(), pet.getProvinceId(), pet.getDepartmentId(), photos, null);
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (InvalidImageQuantityException | DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if (!opPet.isPresent()) {
            LOGGER.warn("Pet update failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI petUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(opPet.get().getId())).build();
        return Response.created(petUri).build();
    }

    @GET
    @Path("/{petId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPet(@Context HttpServletRequest httpRequest, @PathParam("petId") Long petId) {
        String locale = ApiUtils.getLocale(httpRequest);
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        
        Pet pet = opPet.get();
        PetDto petDto = PetDto.fromPet(pet, uriInfo);
        return Response.ok(new GenericEntity<PetDto>(petDto) {}).build();
    }

    @GET
    @Path("/{petId}/images")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getImages(@Context HttpServletRequest httpRequest, @PathParam("petId") long petId) {
        String locale = ApiUtils.getLocale(httpRequest);
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<Long> images = new ArrayList<>();
        opPet.get().getImages().stream().map(img -> images.add(img.getId()));
        return Response.ok(new GenericEntity<List<Long>>(images) {}).build();
    }

    @GET
    @Path("/images/{imageId}")
    @Produces("image/jpg")
    public Response getImage(@PathParam("imageId") long imageId) throws IOException {
        byte[] byteImage = imageService.getDataById(imageId).orElse(null);
        if(byteImage == null) {
            LOGGER.debug("Image {} not found", imageId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
        BufferedImage bufferedImage = ImageIO.read(bis);
        int height = bufferedImage.getHeight(), width = bufferedImage.getWidth();

        BufferedImage cropped = bufferedImage;
        int diff = Math.abs(height-width);
        if(width>height){
            cropped = bufferedImage.getSubimage(diff/2, 0, width-diff, height);
        }
        else{ if(width<height)
            cropped = bufferedImage.getSubimage(0, diff/2, width, height-diff);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cropped, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray(); 
        baos.close();
        return Response.ok(imageInByte).build();
    }

    @POST
    @Path("/{petId}/remove")
    public Response petUpdateRemove(@Context HttpServletRequest httpRequest, @PathParam("petId") Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);
        if(loggedUser == null) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if(petId == null) {
            LOGGER.warn("Invalid pet {}", petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        try {
            petService.removePet(petId, loggedUser.getId());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch(PetException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/{petId}/recover")
    public Response petUpdateRecover(@Context HttpServletRequest httpRequest, @PathParam("petId") Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(httpRequest, userService, auth);
        if(loggedUser == null) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if( petId == null) {
            LOGGER.warn("Invalid pet {}", petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        try {
            petService.recoverPet(petId, loggedUser.getId());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch(PetException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        return Response.ok().build();
    }
}

