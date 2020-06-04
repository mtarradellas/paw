package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exception.InvalidImageQuantityException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.controller.ParentController;
import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.form.AdminUploadPetForm;
import ar.edu.itba.paw.webapp.form.EditPetForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminPetController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPetController.class);

    @Autowired
    private PetService petService;

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    /* TODO placeholders for max number (no pagination) */
    private static final int PAGE = 1;
    private static final int PAGE_MAX = 500;

    private static final int PET_PAGE_SIZE = 25;

    @RequestMapping(value = "/admin/pets")
    public ModelAndView getPetsAdmin(@RequestParam(name = "species", required = false) String species,
                                     @RequestParam(name = "breed", required = false) String breed,
                                     @RequestParam(name = "gender", required = false) String gender,
                                     @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                     @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                     @RequestParam(name = "find", required = false) String find,
                                     @RequestParam(name = "page", required = false) String page,
                                     @RequestParam(name = "priceRange", required = false) String priceRange,
                                     @RequestParam(name = "province", required = false) String province,
                                     @RequestParam(name = "department", required = false) String department,
                                     @RequestParam(name = "status", required = false) String status) {

        ModelAndView mav = new ModelAndView("admin/admin_pets");
        final String locale = getLocale();

        int pageNum = parsePage(page);
        PetStatus petStatus = parseStatus(PetStatus.class, status);
        Long speciesId = parseSpecies(species);
        Long breedId = parseSpecies(breed);
        gender = parseGender(gender);
        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);
        int[] price = parseRange(priceRange);
        int minPriceNum = price[0];
        int maxPriceNum = price[1];
        Long provinceId = parseProvince(province);
        Long departmentId = parseDepartment(department);

        List<String> findList = null;
        if (!isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
            findList = parseFind(find);
        }

        List<Pet> petList = petService.filteredList(locale, findList, null, speciesId, breedId, gender, petStatus, searchCriteria,
                searchOrder, minPriceNum, maxPriceNum, provinceId, departmentId, pageNum, PET_PAGE_SIZE);
        int amount = petService.getFilteredListAmount(locale, findList, null, speciesId, breedId, gender, petStatus, minPriceNum,
                maxPriceNum, provinceId, departmentId);

        List<Breed> breedL = petList.stream().map(Pet::getBreed).distinct().sorted(Breed::compareTo).collect(Collectors.toList());
        List<Species> speciesL = petList.stream().map(Pet::getSpecies).distinct().sorted(Species::compareTo).collect(Collectors.toList());

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
        mav.addObject("petList", petList);
        mav.addObject("amount", amount);
        mav.addObject("speciesList", speciesL);
        mav.addObject("breedList", breedL);
        return mav;
    }

    @RequestMapping(value = "/admin/pet/{id}")
    public ModelAndView getSinglePet(@PathVariable("id") long id){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_pet");

        mav.addObject("pet", petService.findById(getLocale(), id).orElseThrow(PetNotFoundException::new));

        return mav;
    }


    @RequestMapping(value ="/admin/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute("adminUploadPetForm") final AdminUploadPetForm userForm) {
        ModelAndView mav = new ModelAndView("admin/admin_upload_pet");
        String locale = getLocale();

        List<Species> speciesList = speciesService.speciesList(locale);
        List<Breed> breedList = speciesService.breedList(locale);

        List<Department> departmentList = locationService.departmentList();
        List<Province> provinceList = locationService.provinceList();

        List<User> userList = userService.list(PAGE, PAGE_MAX);

        mav.addObject("provinceList", provinceList);
        mav.addObject("departmentList", departmentList);
        mav.addObject("speciesList", speciesList);
        mav.addObject("breedList", breedList);
        mav.addObject("userList", userList);
        return mav;
    }

    @RequestMapping(value = "/admin/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("adminUploadPetForm") final AdminUploadPetForm petForm,
                                  final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadPetForm(petForm);
        }

        Date birthDate = new Date(petForm.getBirthDate().getTime());

        List<byte[]> photos = new ArrayList<>();
        try {
            for (MultipartFile photo : petForm.getPhotos()) {
                try {
                    photos.add(photo.getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new ImageLoadException(ex);
                }
            }
        } catch (ImageLoadException ex) {
            LOGGER.warn("Image bytes load from pet form failed");
            return uploadPetForm(petForm).addObject("imageError", true);
        }

        Optional<Pet> opPet;
        try {
            opPet = petService.create(getLocale(), petForm.getPetName(), birthDate, petForm.getGender(),
                    petForm.getVaccinated(), petForm.getPrice(), petForm.getDescription(), PetStatus.AVAILABLE, petForm.getOwner(),
                    petForm.getSpeciesId(), petForm.getBreedId(), petForm.getProvince(), petForm.getDepartment(), photos);

        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return uploadPetForm(petForm).addObject("petError", true);
        }

        if (!opPet.isPresent()) {
            LOGGER.warn("Pet could not be created");
            return uploadPetForm(petForm).addObject("petError", true);
        }


        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
    }

    @RequestMapping(value = "/admin/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        petService.adminRemovePet(id);
        LOGGER.debug("Pet {} updated as removed", id);
        return new ModelAndView("redirect:/admin/pets");

    }

    @RequestMapping(value = "/admin/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        petService.adminSellPet(id);
        LOGGER.debug("Pet {} updated as sold", id);
        return new ModelAndView("redirect:/admin/pets");
    }

    @RequestMapping(value = "/admin/pet/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
        petService.adminRecoverPet(id);
        LOGGER.debug("Pet {} updated as recovered", id);
        return new ModelAndView("redirect:/admin/pets");
    }

    @RequestMapping(value = "/admin/pet/{id}/edit-pet", method = { RequestMethod.GET })
    public ModelAndView editPet(@ModelAttribute("editPetForm") final EditPetForm petForm, @PathVariable("id") long id){
        Pet pet = petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new);

        petForm.setBirthDate(pet.getBirthDate());
        petForm.setBreedId(pet.getBreed().getId());
        petForm.setDescription(pet.getDescription());
        petForm.setGender(pet.getGender());
        petForm.setProvince(pet.getProvince().getId());
        petForm.setDepartment(pet.getDepartment().getId());
        petForm.setPrice(pet.getPrice());
        petForm.setPetName(pet.getPetName());
        petForm.setSpeciesId(pet.getSpecies().getId());
        petForm.setVaccinated(pet.isVaccinated());

        return editPetForm(petForm, id);
    }

    private ModelAndView editPetForm(@ModelAttribute("editPetForm") final EditPetForm editPetForm, long id) {
        String locale = getLocale();

        List<Species> speciesList = speciesService.speciesList(locale);
        List<Breed> breedList = speciesService.breedList(locale);
        List<Province> provinceList = locationService.provinceList();
        List<Department> departmentList = locationService.departmentList();
        Pet pet = petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new);

        return new ModelAndView("admin/admin_edit_pet")
                .addObject("speciesList", speciesList)
                .addObject("breedList", breedList)
                .addObject("pet", pet)
                .addObject("id", id)
                .addObject("provinceList", provinceList)
                .addObject("departmentList", departmentList);
    }

    @RequestMapping(value = "/admin/pet/{id}/edit", method = { RequestMethod.POST })
    public ModelAndView editPet(@Valid @ModelAttribute("editPetForm") final EditPetForm editPetForm,
                                final BindingResult errors, HttpServletRequest request,
                                @PathVariable("id") long id) {

        if (errors.hasErrors()) {
            return editPetForm(editPetForm, id);
        }
        List<byte[]> photos = new ArrayList<>();
        try {
            for (MultipartFile photo : editPetForm.getPhotos()) {
                if(!photo.isEmpty()) {
                    try {
                        photos.add(photo.getBytes());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new ImageLoadException(ex);
                    }
                }
            }
        } catch (ImageLoadException ex) {
            LOGGER.warn("Image bytes load from pet form failed");
            return editPetForm(editPetForm, id).addObject("imageError", true);
        }

        Optional<Pet> opPet;
        try {
            opPet = petService.update(getLocale(), id, null, editPetForm.getPetName(), editPetForm.getBirthDate(), editPetForm.getGender(),
                    editPetForm.getVaccinated(), editPetForm.getPrice(), editPetForm.getDescription(), null, editPetForm.getSpeciesId(),
                    editPetForm.getBreedId(), editPetForm.getProvince(), editPetForm.getDepartment(), photos, editPetForm.getImagesIdToDelete());

        } catch(InvalidImageQuantityException ex) {
            LOGGER.warn(ex.getMessage());
            return editPetForm(editPetForm, id).addObject("imageQuantityError", true);

        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return editPetForm(editPetForm, id).addObject("petError", true);
        }

        if(!opPet.isPresent()){
            LOGGER.warn("Pet could not be updated");
            return editPetForm(editPetForm, id).addObject("petError", true);
        }
        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
    }

}
