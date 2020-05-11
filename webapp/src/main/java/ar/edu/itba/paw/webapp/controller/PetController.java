package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.form.UploadPetForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Controller
public class PetController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                @RequestParam(name = "breed", required = false) String breed,
                                @RequestParam(name = "gender", required = false) String gender,
                                @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                @RequestParam(name = "find", required = false) String findValue,
                                @RequestParam(name = "page", required = false) String page,
                                @RequestParam(name = "minPrice", required = false) String minPrice,
                                @RequestParam(name = "maxPrice", required = false) String maxPrice) {

        final ModelAndView mav = new ModelAndView("index");
        final String locale = getLocale();

        if (page == null) {
            page = "1";
        }

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        PetList petList = petService.petList(locale, findValue, species, breed, gender, searchCriteria,
                searchOrder, minPrice, maxPrice, page);

        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petList.getMaxPage());
        mav.addObject("home_pet_list", petList.toArray());
        mav.addObject("species_list", petList.getSpecies().toArray());
        mav.addObject("breeds_list", petList.getBreeds().toArray());
        mav.addObject("pets_list_size", petList.size());
        return mav;
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        User user = loggedUser();
        String locale = getLocale();
        /* Check if user has already requested pet */
        if (user != null && !user.getRequestList().isEmpty()) {
            Optional<Request> opRequest = user.getRequestList().stream().filter(request -> request.getPetId() == id).max(Comparator.comparing(Request::getCreationDate));
            if (!opRequest.isPresent()) {
                LOGGER.debug("User {} has no request for pet {}", user.getId(), id);
                mav.addObject("lastRequest", null);
                mav.addObject("requestExists", false);
            }
            else {
                LOGGER.debug("User {} last request status for pet {} is {}", user.getId(), id, opRequest.get().getId());
                mav.addObject("lastRequest", opRequest.get().getStatus().getId());
                mav.addObject("requestExists", true);
            }
        } else {
            LOGGER.debug("User is not authenticated or has no requests");
            mav.addObject("lastRequest", null);
            mav.addObject("requestExists", false);
        }
        Pet pet = petService.findById(locale, id).orElseThrow(PetNotFoundException::new);

        if(pet.getOwnerId() == user.getId() || pet.getStatus().getId() == PetStatus.AVAILABLE.getValue()) {
            mav.addObject("pet", pet);
        }
        else {
            throw new PetNotFoundException();
        }
        return mav;
    }

    @RequestMapping(value = "/pet/{id}/request", method = {RequestMethod.POST})
    public ModelAndView requestPet(@PathVariable("id") final long id) {
        final ModelAndView mav = new ModelAndView("redirect:/pet/" + id);
        final User user = loggedUser();
        final String locale = getLocale();

        if (user == null) {
            LOGGER.warn("User not authenticated, ignoring request");
            return new ModelAndView("redirect:/403");
        }

        /* TODO Generate exceptions for error handling */

        Optional<Request> opRequest =  requestService.create(user.getId(), id, locale);
        if (!opRequest.isPresent()) {
            mav.addObject("request_error", true);
        }

        return new ModelAndView("redirect:/pet/" + id );
    }

    @RequestMapping(value = "/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.sellPet(id, user.getId())) {
            LOGGER.debug("Pet {} updated as sold", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.removePet(id, user.getId())) {
            LOGGER.debug("Pet {} updated as removed", id);
            return new ModelAndView("redirect:/");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/pet/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && petService.recoverPet(id, user.getId())) {
            LOGGER.debug("Pet {} updated as recovered", id);
            return new ModelAndView("redirect:/pet/{id}");
        }
        LOGGER.warn("User is not pet owner, pet status not updated");
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable("id") long id) {
        return imageService.getDataById(id).orElse(null);
    }

    @RequestMapping(value ="/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute ("uploadPetForm") final UploadPetForm petForm) {
        ModelAndView mav = new ModelAndView("views/upload_pet");
        String locale = getLocale();

        BreedList breedList = speciesService.breedsList(locale);
        mav.addObject("species_list", breedList.getSpecies().toArray());
        mav.addObject("breeds_list", breedList.toArray());
        return mav;
    }

    @RequestMapping(value = "/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("uploadPetForm") final UploadPetForm petForm,
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
            return uploadPetForm(petForm).addObject("image_error", true);
        }

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getSpeciesId(), petForm.getBreedId(),
                          petForm.getLocation(), petForm.getVaccinated(), petForm.getGender(), petForm.getDescription(),
                          birthDate, currentDate, petForm.getPrice(), loggedUser().getId(), photos);

        if (!opPet.isPresent()) {
            LOGGER.warn("Pet could not be created");
            return uploadPetForm(petForm).addObject("pet_error", true);
        }

        return new ModelAndView("redirect:/pet/" + opPet.get().getId());
    }
}
