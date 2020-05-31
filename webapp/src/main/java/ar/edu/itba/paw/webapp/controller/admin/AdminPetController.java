package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.exception.InvalidImageQuantityException;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.UserStatus;
import ar.edu.itba.paw.webapp.controller.ParentController;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.form.AdminUploadPetForm;
import ar.edu.itba.paw.webapp.form.EditPetForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminPetController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPetController.class);

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
                                     @RequestParam(name = "minPrice", required = false) String minPrice,
                                     @RequestParam(name = "maxPrice", required = false) String maxPrice,
                                     @RequestParam(name = "status", required = false) String status) {

        ModelAndView mav = new ModelAndView("admin/admin_pets");
        final String locale = getLocale();

        int pageNum = parsePage(page);
        PetStatus petStatus = parseStatus(PetStatus.class, status);
        int speciesId = parseSpecies(species);

        if (!parseFind(find)) {
            mav.addObject("wrongSearch", true);
            find = "";
        } else {
            mav.addObject("wrongSearch", false);
        }

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        status = status == null || status.equals("any") ? null : status;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        PetList petList = petService.adminPetList(locale, find, species, breed, gender, status, searchCriteria,
                searchOrder, minPrice, maxPrice, page);

        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petList.getMaxPage());
        mav.addObject("petList", petList);
        mav.addObject("speciesList", petList.getSpecies());
        mav.addObject("breedList", petList.getBreeds());
        return mav;
    }

    @RequestMapping(value = "/admin/pet/{id}")
    public ModelAndView getSinglePet(@PathVariable("id") long id){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_pet");

        mav.addObject("pet", petService.adminFindById(getLocale(), id).orElseThrow(PetNotFoundException::new));

        return mav;
    }


    @RequestMapping(value ="/admin/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute("adminUploadPetForm") final AdminUploadPetForm userForm) {
        ModelAndView mav = new ModelAndView("admin/admin_upload_pet");
        String locale = getLocale();

        List<Species> speciesList = speciesService.speciesList(locale);
        List<Breed> breedList = speciesService.breedList(locale);

        DepartmentList departmentList = locationService.departmentList();

        mav.addObject("provinceList", departmentList.getProvinceList().toArray());
        mav.addObject("departmentList", departmentList.toArray());
        mav.addObject("speciesList", speciesList.toArray());
        mav.addObject("breedList", breedList.toArray());
        mav.addObject("userList", userService.list(PAGE, PAGE_MAX).toArray());
        return mav;
    }

    @RequestMapping(value = "/admin/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("adminUploadPetForm") final AdminUploadPetForm petForm,
                                  final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadPetForm(petForm);
        }

        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        Date birthDate = new java.sql.Date(petForm.getBirthDate().getTime());

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

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getSpeciesId(), petForm.getBreedId(),
                petForm.getVaccinated(), petForm.getGender(), petForm.getDescription(),
                birthDate, currentDate, petForm.getPrice(), petForm.getOwner(), petForm.getDepartment(), photos);

        if (!opPet.isPresent()) {
            LOGGER.warn("Pet could not be created");
            return uploadPetForm(petForm).addObject("petError", true);
        }


        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
    }

    @RequestMapping(value = "/admin/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        String locale = getLocale();
        petService.removePetAdmin(locale, id);
        LOGGER.debug("Pet {} updated as removed", id);
        return new ModelAndView("redirect:/admin/pets");

    }

    @RequestMapping(value = "/admin/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        petService.sellPetAdmin(id);
        LOGGER.debug("Pet {} updated as sold", id);
        return new ModelAndView("redirect:/admin/pets");
    }

    @RequestMapping(value = "/admin/pet/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
        petService.recoverPetAdmin(id);
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
        ProvinceList provinceList = locationService.provinceList();
        DepartmentList departmentList = locationService.departmentList();

        return new ModelAndView("admin/admin_edit_pet")
                .addObject("speciesList", speciesList.toArray())
                .addObject("breedList", breedList.toArray())
                .addObject("pet",
                        petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new))
                .addObject("id", id)
                .addObject("provinceList", provinceList.toArray())
                .addObject("departmentList", departmentList.toArray());
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

        Date birthDate = new java.sql.Date(editPetForm.getBirthDate().getTime());
        Optional<Pet> opPet;
        try {
            opPet = petService.adminUpdate(getLocale(), loggedUser().getId(), id, photos, editPetForm.getImagesIdToDelete(),
                    editPetForm.getPetName(), editPetForm.getSpeciesId(), editPetForm.getBreedId(), editPetForm.getVaccinated(),
                    editPetForm.getGender(), editPetForm.getDescription(), birthDate, editPetForm.getPrice(), editPetForm.getDepartment());
        }
        catch(InvalidImageQuantityException ex) {
            LOGGER.warn(ex.getMessage());

            return editPetForm(editPetForm, id).addObject("imageQuantityError", true);
        }
        if(!opPet.isPresent()){
            LOGGER.warn("Pet could not be updated");
            return new ModelAndView("redirect:/admin/pets");
        }
        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
    }

}
