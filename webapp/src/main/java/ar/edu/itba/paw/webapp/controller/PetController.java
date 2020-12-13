package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.PetException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/pets")
public class PetController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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
    public Response getPets(@QueryParam("ownerId") @DefaultValue("0") Long ownerId,
                            @QueryParam("species") @DefaultValue("0") Long species,
                            @QueryParam("breed") @DefaultValue("0") Long breed,
                            @QueryParam("province") @DefaultValue("0") Long province,
                            @QueryParam("department") @DefaultValue("0") Long department,
                            @QueryParam("gender") String gender,
                            @QueryParam("searchCriteria") String searchCriteria,
                            @QueryParam("find") String find,
                            @QueryParam("searchOrder") String searchOrder,
                            @QueryParam("priceRange") int priceRange,
                            @QueryParam("page") @DefaultValue("1") int page) {

        final String locale = ApiUtils.getLocale();
        int[] range;
        try {
            ownerId = ParseUtils.parseUserId(ownerId);
            ParseUtils.parsePage(page);
            ParseUtils.isAllowedFind(find);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
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
        List<PetDto> petList;
        try {
            petList = petService.filteredList(locale, findList, ownerId, species, breed, gender, PetStatus.AVAILABLE,
                    searchCriteria, searchOrder, minPrice, maxPrice, province, department, page, PET_PAGE_SIZE)
                    .stream().map(p -> PetDto.fromPetForList(p, uriInfo)).collect(Collectors.toList());
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        final int amount = petService.getListAmount();
        return ApiUtils.paginatedListResponse(amount, PET_PAGE_SIZE, page, uriInfo, new GenericEntity<List<PetDto>>(petList) {});
    }

    @GET
    @Path("/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPets(@QueryParam("ownerId") @DefaultValue("0") long ownerId,
                            @QueryParam("species") @DefaultValue("0") Long species,
                            @QueryParam("breed") @DefaultValue("0") Long breed,
                            @QueryParam("province") @DefaultValue("0") Long province,
                            @QueryParam("department") @DefaultValue("0") Long department,
                            @QueryParam("gender") String gender,
                            @QueryParam("find") String find,
                            @QueryParam("priceRange") int priceRange) {

        final String locale = ApiUtils.getLocale();
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
            breeds = petService.filteredBreedList(locale, findList, null, species, breed, gender, PetStatus.AVAILABLE,
                 minPrice, maxPrice, province, department);
            departments = petService.filteredDepartmentList(locale, findList, null, species, breed, gender, PetStatus.AVAILABLE,
                    minPrice, maxPrice, province, department);
            rangeList = petService.filteredRangesList(locale, findList, null, species, breed, gender, PetStatus.AVAILABLE,
                    minPrice, maxPrice, province, department);
            genderList = petService.filteredGenderList(locale, findList, null, species, breed, gender, PetStatus.AVAILABLE,
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
    public Response create(final PetDto pet) {
        String locale = ApiUtils.getLocale();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(userService, auth);
        Optional<Pet> opNewPet;

        /* TODO como recibir las fotos ?*/
        try {
            opNewPet = petService.create(locale, pet.getPetName(), pet.getBirthDate(), pet.getGender(), pet.isVaccinated(),
                    pet.getPrice(), pet.getDescription(), PetStatus.AVAILABLE, loggedUser.getId(), pet.getSpeciesId(), pet.getBreedId(),
                    pet.getProvinceId(), pet.getDepartmentId(),null);
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
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

    @GET
    @Path("/{petId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getPet(@PathParam("petId") Long petId) {
        String locale = ApiUtils.getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        Pet pet = opPet.get();
        if(pet.getStatus() == PetStatus.REMOVED || pet.getStatus() == PetStatus.UNAVAILABLE) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        PetDto petDto = PetDto.fromPet(pet, uriInfo);
        return Response.ok(new GenericEntity<PetDto>(petDto) {}).build();
    }

    @GET
    @Path("/{petId}/images")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getImages(@PathParam("petId") long petId) {
        String locale = ApiUtils.getLocale();
        Optional<Pet> opPet = petService.findById(locale, petId);
        if(!opPet.isPresent()) {
            LOGGER.debug("Pet {} not found", petId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        List<ImageDTO> images = opPet.get().getImages();
        return Response.ok(new GenericEntity<List<ImageDTO>>(images) {}).build();
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
    @Path("/{petId}/sell-adopt")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response petUpdateSold(@PathParam("petId") Long petId,
                                  final UserDto newOwner) {
        Long newOwnerId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(userService, auth);
        try {
            newOwnerId = ParseUtils.parseUserId(newOwner.getId());
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if(newOwnerId == null || loggedUser == null || petId == null) {
            LOGGER.warn("Invalid parameters. New owner {}, logged user {}, pet {}", newOwnerId, loggedUser, petId);
            final ErrorDto body = new ErrorDto(2, "Invalid parameters");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        boolean sold;
        try {
            sold = petService.sellPet(petId, loggedUser.getId(), newOwnerId, uriInfo.getBaseUri().toString());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        if(!sold) {
            LOGGER.warn("Pet status not updated");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/{petId}/remove")
    public Response petUpdateRemove(@PathParam("petId") Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(userService, auth);
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if(loggedUser == null || petId == null) {
            LOGGER.warn("Invalid parameters. Logged user {}, pet {}", loggedUser, petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        boolean removed;
        try {
            removed = petService.removePet(petId, loggedUser.getId());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        if(!removed) {
            LOGGER.warn("Pet status not updated");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/{petId}/recover")
    public Response petUpdateRecover(@PathParam("petId") Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ApiUtils.loggedUser(userService, auth);
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(loggedUser == null || petId == null) {
            LOGGER.warn("Invalid parameters. Logged user {}, pet {}", loggedUser, petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        boolean recovered;
        try {
            recovered = petService.recoverPet(petId, loggedUser.getId());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        if(!recovered) {
            LOGGER.warn("Pet status not updated");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        return Response.ok().build();
    }
}


//
//
//    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.GET })
//    public ModelAndView editPetGet(@ModelAttribute("editPetForm") final EditPetForm petForm, @PathVariable("id") long id) {
//
//        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);
//
//        if(pet.getUser().getId().equals(loggedUser().getId())) {
//
//            List<Department> departmentList = locationService.departmentList();
//            List<Province> provinceList = locationService.provinceList();
//
//            petForm.setBirthDate(java.util.Date.from(pet.getBirthDate().atZone(ZoneId.systemDefault()).toInstant()));
//            petForm.setBreedId(pet.getBreed().getId());
//            petForm.setDescription(pet.getDescription());
//            petForm.setGender(pet.getGender());
//            petForm.setProvince(pet.getProvince().getId());
//            petForm.setDepartment(pet.getDepartment().getId());
//            petForm.setPrice(pet.getPrice());
//            petForm.setPetName(pet.getPetName());
//            petForm.setSpeciesId(pet.getSpecies().getId());
//            petForm.setVaccinated(pet.isVaccinated());
//
//            return editPetForm(petForm, id)
//                    .addObject("provinceList", provinceList)
//                    .addObject("departmentList", departmentList);
//        }
//        return new ModelAndView("redirect:/403" );
//
//    }
//
//    private ModelAndView editPetForm(@ModelAttribute("editPetForm") final EditPetForm editPetForm, long id) {
//        String locale = getLocale();
//
//        Pet pet = petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new);
//        List<Species> speciesList = speciesService.speciesList(locale);
//        List<Breed> breedList = speciesService.breedList(locale);
//        List<Department> departmentList = locationService.departmentList();
//        List<Province> provinceList = locationService.provinceList();
//
//        return new ModelAndView("views/pet_edit")
//                .addObject("speciesList", speciesList)
//                .addObject("breedList", breedList)
//                .addObject("provinceList", provinceList)
//                .addObject("departmentList", departmentList)
//                .addObject("pet", pet)
//                .addObject("id", id);
//    }
//
//    @RequestMapping(value = "/edit-pet/{id}", method = { RequestMethod.POST })
//    public ModelAndView editPet(@Valid @ModelAttribute("editPetForm") final EditPetForm editPetForm,
//                                  final BindingResult errors, HttpServletRequest request,
//                                @PathVariable("id") long id) {
//        User user = loggedUser();
//        if (user == null) throw new UserNotFoundException();
//        String locale = getLocale();
//
//        if (errors.hasErrors()) {
//            return editPetForm(editPetForm, id);
//        }
//        List<byte[]> photos = new ArrayList<>();
//        try {
//            for (MultipartFile photo : editPetForm.getPhotos()) {
//                if(!photo.isEmpty()) {
//                    try {
//                        photos.add(photo.getBytes());
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                        throw new ImageLoadException(ex);
//                    }
//                }
//            }
//        } catch (ImageLoadException ex) {
//            LOGGER.warn("Image bytes load from pet form failed");
//            return editPetForm(editPetForm, id).addObject("imageError", true);
//        }
//
//        Optional<Pet> opPet;
//        try {
//            LocalDateTime birthDate = editPetForm.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//             opPet = petService.update(locale, id, user.getId(), editPetForm.getPetName(), birthDate,
//                     editPetForm.getGender(), editPetForm.getVaccinated(), editPetForm.getPrice(), editPetForm.getDescription(),
//                     null, editPetForm.getSpeciesId(), editPetForm.getBreedId(), editPetForm.getProvince(),
//                     editPetForm.getDepartment(), photos, editPetForm.getImagesIdToDelete());
//
//        } catch (InvalidImageQuantityException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return editPetForm(editPetForm, id).addObject("imageQuantityError", true);
//
//        } catch (DataIntegrityViolationException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return editPetForm(editPetForm, id).addObject("petError", true);
//        }
//
//        if(!opPet.isPresent()){
//            LOGGER.warn("Pet could not be updated");
//            return editPetForm(editPetForm, id).addObject("petError", true);
//        }
//        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
//    }

//
//}
