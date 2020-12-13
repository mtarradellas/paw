package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.interfaces.UserService;
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
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/admin/pets")
public class AdminPetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPetController.class);

    @Autowired
    private PetService petService;

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    private static final int PET_PAGE_SIZE = 25;

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
                            @QueryParam("status") int status,
                            @QueryParam("page") @DefaultValue("1") int page) {

        final String locale = ApiUtils.getLocale();
        int[] range;
        PetStatus petStatus;
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
            petStatus = ParseUtils.parseStatus(PetStatus.class, status);
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
            petList = petService.filteredList(locale, findList, ownerId, species, breed, gender, petStatus,
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
                            @QueryParam("status") int status,
                            @QueryParam("priceRange") int priceRange) {

        final String locale = ApiUtils.getLocale();
        int[] range;
        Long owner;
        PetStatus petStatus;
        try {
            owner = ParseUtils.parseUserId(ownerId);
            species = ParseUtils.parseSpecies(species);
            breed = ParseUtils.parseBreed(breed);
            province = ParseUtils.parseProvince(province);
            department = ParseUtils.parseDepartment(department);
            gender = ParseUtils.parseGender(gender);
            range = ParseUtils.parseRange(priceRange);
            petStatus = ParseUtils.parseStatus(PetStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body) {
                    }).build();
        }
        int minPrice = range[0];
        int maxPrice = range[1];
        List<String> findList = ParseUtils.parseFind(find);
        Set<String> genderList;
        Set<Integer> rangeList;
        List<Department> departments;
        List<Breed> breeds;
        try {
            breeds = petService.filteredBreedList(locale, findList, null, species, breed, gender, petStatus,
                    minPrice, maxPrice, province, department);
            departments = petService.filteredDepartmentList(locale, findList, null, species, breed, gender, petStatus,
                    minPrice, maxPrice, province, department);
            rangeList = petService.filteredRangesList(locale, findList, null, species, breed, gender, petStatus,
                    minPrice, maxPrice, province, department);
            genderList = petService.filteredGenderList(locale, findList, null, species, breed, gender, petStatus,
                    minPrice, maxPrice, province, department);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body) {
                    }).build();
        }
        List<SpeciesDto> speciesList = breeds.stream().map(Breed::getSpecies).distinct().sorted(Species::compareTo)
                .map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
        List<ProvinceDto> provinceList = departments.stream().map(Department::getProvince).distinct().sorted(Province::compareTo)
                .map(ProvinceDto::fromProvince).collect(Collectors.toList());
        List<DepartmentDto> departmentList = departments.stream().map(d -> DepartmentDto.fromDepartment(d, uriInfo)).collect(Collectors.toList());
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
        PetDto petDto = PetDto.fromPet(opPet.get(), uriInfo);
        return Response.ok(new GenericEntity<PetDto>(petDto) {}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response create(final PetDto pet) {
        String locale = ApiUtils.getLocale();
        Optional<Pet> opNewPet;
        /* TODO como recibir las fotos ?*/
        PetStatus petStatus;
        try {
            petStatus = ParseUtils.parseStatus(PetStatus.class, pet.getStatus());
            opNewPet = petService.create(locale, pet.getPetName(), pet.getBirthDate(), pet.getGender(), pet.isVaccinated(),
                    pet.getPrice(), pet.getDescription(), petStatus, pet.getUserId(), pet.getSpeciesId(), pet.getBreedId(),
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
    //////////////////////////////////////////////////////////////

    @POST
    @Path("/{petId}/sell-adopt")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response petUpdateSold(@PathParam("petId") Long petId,
                                  final UserDto newOwner) {
        Long newOwnerId;
        try {
            newOwnerId = ParseUtils.parseUserId(newOwner.getId());
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if(newOwnerId == null || petId == null) {
            LOGGER.warn("Invalid parameters. New owner {}, pet {}", newOwnerId, petId);
            final ErrorDto body = new ErrorDto(2, "Invalid parameters");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        boolean sold;
        try {
            sold = petService.adminSellPet(petId, newOwnerId);
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
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if(petId == null) {
            LOGGER.warn("Invalid parameter: Pet {}", petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        boolean removed;
        try {
            removed = petService.adminRemovePet(petId);
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
        try {
            petId = ParseUtils.parsePetId(petId);
        } catch(BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(petId == null) {
            LOGGER.warn("Invalid parameter: Pet {}", petId);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        boolean recovered;
        try {
            recovered = petService.adminRecoverPet(petId);
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
//    @RequestMapping(value = "/admin/pet/{id}/edit-pet", method = { RequestMethod.GET })
//    public ModelAndView editPet(@ModelAttribute("editPetForm") final EditPetForm petForm, @PathVariable("id") long id){
//        Pet pet = petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new);
//
//        petForm.setBirthDate(java.util.Date.from(pet.getBirthDate().atZone(ZoneId.systemDefault()).toInstant()));
//        petForm.setBreedId(pet.getBreed().getId());
//        petForm.setDescription(pet.getDescription());
//        petForm.setGender(pet.getGender());
//        petForm.setProvince(pet.getProvince().getId());
//        petForm.setDepartment(pet.getDepartment().getId());
//        petForm.setPrice(pet.getPrice());
//        petForm.setPetName(pet.getPetName());
//        petForm.setSpeciesId(pet.getSpecies().getId());
//        petForm.setVaccinated(pet.isVaccinated());
//
//        return editPetForm(petForm, id);
//    }
//
//    private ModelAndView editPetForm(@ModelAttribute("editPetForm") final EditPetForm editPetForm, long id) {
//        String locale = getLocale();
//
//        List<Species> speciesList = speciesService.speciesList(locale);
//        List<Breed> breedList = speciesService.breedList(locale);
//        List<Province> provinceList = locationService.provinceList();
//        List<Department> departmentList = locationService.departmentList();
//        Pet pet = petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new);
//
//        return new ModelAndView("admin/admin_edit_pet")
//                .addObject("speciesList", speciesList)
//                .addObject("breedList", breedList)
//                .addObject("pet", pet)
//                .addObject("id", id)
//                .addObject("provinceList", provinceList)
//                .addObject("departmentList", departmentList);
//    }
//
//    @RequestMapping(value = "/admin/pet/{id}/edit", method = { RequestMethod.POST })
//    public ModelAndView editPet(@Valid @ModelAttribute("editPetForm") final EditPetForm editPetForm,
//                                final BindingResult errors, HttpServletRequest request,
//                                @PathVariable("id") long id) {
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
//            opPet = petService.update(getLocale(), id, null, editPetForm.getPetName(), birthDate, editPetForm.getGender(),
//                    editPetForm.getVaccinated(), editPetForm.getPrice(), editPetForm.getDescription(), null, editPetForm.getSpeciesId(),
//                    editPetForm.getBreedId(), editPetForm.getProvince(), editPetForm.getDepartment(), photos, editPetForm.getImagesIdToDelete());
//
//        } catch(InvalidImageQuantityException ex) {
//            LOGGER.warn(ex.getMessage());
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
//        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
//    }
//
//}
