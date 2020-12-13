package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        int minPrice = range[0];
        int maxPrice = range[1];
        List<String> findList = ParseUtils.parseFind(find);
        List<PetDto> petList;
        try {
            petList = petService.filteredList(locale, findList, ownerId, species, breed, gender, null,
                    searchCriteria, searchOrder, minPrice, maxPrice, province, department, page, PET_PAGE_SIZE)
                    .stream().map(p -> PetDto.fromPetForList(p, uriInfo)).collect(Collectors.toList());
        } catch(NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final int amount = petService.getListAmount();
        return ApiUtils.paginatedListResponse(amount, PET_PAGE_SIZE, page, uriInfo, new GenericEntity<List<PetDto>>(petList) {});
    }

}
//    @RequestMapping(value = "/admin/pets")
//    public ModelAndView getPetsAdmin(@RequestParam(name = "species", required = false) String species,
//                                     @RequestParam(name = "breed", required = false) String breed,
//                                     @RequestParam(name = "gender", required = false) String gender,
//                                     @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
//                                     @RequestParam(name = "searchOrder", required = false) String searchOrder,
//                                     @RequestParam(name = "find", required = false) String find,
//                                     @RequestParam(name = "page", required = false) String page,
//                                     @RequestParam(name = "priceRange", required = false) String priceRange,
//                                     @RequestParam(name = "province", required = false) String province,
//                                     @RequestParam(name = "department", required = false) String department,
//                                     @RequestParam(name = "status", required = false) String status) {
//
//        ModelAndView mav = new ModelAndView("admin/admin_pets");
//        final String locale = getLocale();
//
//        int pageNum = ParseUtils.parsePage(page);
//        PetStatus petStatus = ParseUtils.parseStatus(PetStatus.class, status);
//        Long speciesId = ParseUtils.parseSpecies(species);
//        Long breedId = ParseUtils.parseSpecies(breed);
//        gender = ParseUtils.parseGender(gender);
//        searchCriteria = ParseUtils.parseCriteria(searchCriteria);
//        searchOrder = ParseUtils.parseOrder(searchOrder);
//        int[] price = ParseUtils.parseRange(priceRange);
//        int minPriceNum = price[0];
//        int maxPriceNum = price[1];
//        Long provinceId = ParseUtils.parseProvince(province);
//        Long departmentId = ParseUtils.parseDepartment(department);
//
//        List<String> findList = null;
//        if (!ParseUtils.isAllowedFind(find)) {
//            mav.addObject("wrongSearch", true);
//        } else {
//            mav.addObject("wrongSearch", false);
//            findList = ParseUtils.parseFind(find);
//        }
//
//        List<Pet> petList = petService.filteredList(locale, findList, null, speciesId, breedId, gender, petStatus, searchCriteria,
//                searchOrder, minPriceNum, maxPriceNum, provinceId, departmentId, pageNum, PET_PAGE_SIZE);
//        int amount = petService.getFilteredListAmount(locale, findList, null, speciesId, breedId, gender, petStatus, minPriceNum,
//                maxPriceNum, provinceId, departmentId);
//
//        List<Breed> breedList = petService.filteredBreedList(locale, findList, null, speciesId, breedId, gender, petStatus,
//                minPriceNum, maxPriceNum, provinceId, departmentId);
//        Object[] speciesList = breedList.stream().map(Breed::getSpecies).distinct().sorted(Species::compareTo).toArray();
//
//        mav.addObject("currentPage", pageNum);
//        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
//        mav.addObject("petList", petList);
//        mav.addObject("amount", amount);
//
//        mav.addObject("speciesList", speciesList);
//        mav.addObject("breedList", breedList.toArray());
//
//        mav.addObject("nanStatus", status == null);
//
//        return mav;
//    }
//
//    @RequestMapping(value = "/admin/pet/{id}")
//    public ModelAndView getSinglePet(@PathVariable("id") long id){
//
//        final ModelAndView mav = new ModelAndView("/admin/admin_single_pet");
//
//        mav.addObject("pet", petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new));
//
//        return mav;
//    }
//
//
//    @RequestMapping(value ="/admin/upload-pet", method = { RequestMethod.GET })
//    public ModelAndView uploadPetForm(@ModelAttribute("adminUploadPetForm") final AdminUploadPetForm userForm) {
//        ModelAndView mav = new ModelAndView("admin/admin_upload_pet");
//        String locale = getLocale();
//
//        List<Species> speciesList = speciesService.speciesList(locale);
//        List<Breed> breedList = speciesService.breedList(locale);
//
//        List<Department> departmentList = locationService.departmentList();
//        List<Province> provinceList = locationService.provinceList();
//
//        mav.addObject("provinceList", provinceList);
//        mav.addObject("departmentList", departmentList);
//        mav.addObject("speciesList", speciesList);
//        mav.addObject("breedList", breedList);
//        return mav;
//    }
//
//    @RequestMapping(value = "/admin/upload-pet", method = { RequestMethod.POST })
//    public ModelAndView uploadPet(@Valid @ModelAttribute("adminUploadPetForm") final AdminUploadPetForm petForm,
//                                  final BindingResult errors, HttpServletRequest request) {
//
//
//        if (errors.hasErrors()) {
//            return uploadPetForm(petForm);
//        }
//
//        LocalDateTime birthDate = petForm.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//
//        List<byte[]> photos = new ArrayList<>();
//        try {
//            for (MultipartFile photo : petForm.getPhotos()) {
//                try {
//                    photos.add(photo.getBytes());
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    throw new ImageLoadException(ex);
//                }
//            }
//        } catch (ImageLoadException ex) {
//            LOGGER.warn("Image bytes load from pet form failed");
//            return uploadPetForm(petForm).addObject("imageError", true);
//        }
//
//        Optional<Pet> opPet;
//        try {
//            opPet = petService.create(getLocale(), petForm.getPetName(), birthDate, petForm.getGender(),
//                    petForm.getVaccinated(), petForm.getPrice(), petForm.getDescription(), PetStatus.AVAILABLE, petForm.getOwner(),
//                    petForm.getSpeciesId(), petForm.getBreedId(), petForm.getProvince(), petForm.getDepartment(), photos);
//
//        } catch (DataIntegrityViolationException | UserException ex) {
//            LOGGER.warn("{}", ex.getMessage());
//            return uploadPetForm(petForm)
//                    .addObject("petError", !ex.getMessage().contains("user"))
//                    .addObject("invalidUser", ex.getMessage().contains("user"));
//        }
//
//        if (!opPet.isPresent()) {
//            LOGGER.warn("Pet could not be created");
//            return uploadPetForm(petForm).addObject("petError", true);
//        }
//
//
//        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
//    }
//
//    @RequestMapping(value = "/admin/pet/{id}/remove", method = {RequestMethod.POST})
//    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
//        petService.adminRemovePet(id);
//        LOGGER.debug("Pet {} updated as removed", id);
//        return new ModelAndView("redirect:/admin/pets");
//
//    }
//
//    @RequestMapping(value = "/admin/pet/{id}/sell-adopt", method = {RequestMethod.POST})
//    public ModelAndView petUpdateSold(@PathVariable("id") long id,
//                                      @RequestParam(name = "newowner", required = false) String newOwner) {
//
//        Long newOwnerId = ParseUtils.parseUser(newOwner);
//        if (newOwnerId != null) {
//            petService.adminSellPet(id, newOwnerId);
//            LOGGER.debug("Pet {} updated as sold", id);
//        } else {
//            LOGGER.warn("Invalid target user, pet {} was not sold", id);
//        }
//        return new ModelAndView("redirect:/admin/pets");
//    }
//
//    @RequestMapping(value = "/admin/pet/{id}/recover", method = {RequestMethod.POST})
//    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
//        petService.adminRecoverPet(id);
//        LOGGER.debug("Pet {} updated as recovered", id);
//        return new ModelAndView("redirect:/admin/pets");
//    }
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
