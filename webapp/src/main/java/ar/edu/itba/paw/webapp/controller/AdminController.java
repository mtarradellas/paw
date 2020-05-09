package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AdminUploadPetForm;
import ar.edu.itba.paw.webapp.form.AdminUploadRequestForm;
import ar.edu.itba.paw.webapp.form.UploadPetForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController extends ParentController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    //TODO: cambiar el path del mapping para que te redireccion correctamente
    @RequestMapping(value = "/admi")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }



//    PETS ENDPOINTS
    @RequestMapping(value = "/admi/pets")
    public ModelAndView getPetsAdmin(@RequestParam(name = "species", required = false) String species,
                                     @RequestParam(name = "breed", required = false) String breed,
                                     @RequestParam(name = "gender", required = false) String gender,
                                     @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                     @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                     @RequestParam(name = "status", required = false) String status,
                                     @RequestParam(name = "page", required = false) String page,
                                     @RequestParam(name = "find", required = false) String find) {

        if(page == null){
            page = "1";
        }

        final String locale = getLocale();

        ModelAndView mav = new ModelAndView("admin/admin_pets");

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        status = status == null || status.equals("any") ? null : status;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        if (species != null || gender != null || searchCriteria != null || status != null) {
            String maxPage = petService.getMaxAdminFilterPages(locale, species, breed, gender, status);
            mav.addObject("maxPage", maxPage);

            LOGGER.debug("Requesting filtered pet list of parameters: locale: {}, spec: {}, breed: {}, gender: {}, status: {}, sCriteria: {}, sOrder: {}, page: {}",
                    locale, species, breed, gender, status, searchCriteria, searchOrder, page);
            List<Pet> petList = petService.adminFilteredList(locale, species, breed, gender, status, searchCriteria,
                    searchOrder, page);
            mav.addObject("pets_list", petList);

        }else if(find != null){
            String maxPage = petService.getAdminMaxSearchPages(locale,find);
            mav.addObject("maxPage", maxPage);
            List<Pet> petList = petService.adminSearchList(locale, find, page);
            mav.addObject("pets_list", petList);

        }else{
            String maxPage = petService.getAdminMaxPages();
            mav.addObject("maxPage", maxPage);
            List<Pet> petList = petService.adminList(locale, page);
            mav.addObject("pets_list", petList);
        }

        mav.addObject("currentPage", page);
        mav.addObject("species_list", speciesService.speciesList(locale).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(locale).toArray());



        return mav;
    }

    @RequestMapping(value = "/admi/pet/{id}")
    public ModelAndView getSinglePet(@PathVariable("id") long id){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_pet");

        mav.addObject("pet", petService.adminFindById(getLocale(), id).orElseThrow(PetNotFoundException::new));

        return mav;
    }


    @RequestMapping(value ="/admi/upload-pet", method = { RequestMethod.GET })
    public ModelAndView uploadPetForm(@ModelAttribute("adminUploadPetForm") final AdminUploadPetForm userForm) {
        return new ModelAndView("admin/admin_upload_pet")
                .addObject("species_list", speciesService.speciesList(getLocale()).toArray())
                .addObject("breeds_list", speciesService.breedsList(getLocale()).toArray())
                .addObject("users_list",userService.list().toArray());
    }

    @RequestMapping(value = "/admi/upload-pet", method = { RequestMethod.POST })
    public ModelAndView uploadPet(@Valid @ModelAttribute("adminUploadPetForm") final AdminUploadPetForm petForm,
                                  final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadPetForm(petForm);
        }

        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        Date birthDate = new java.sql.Date(petForm.getBirthDate().getTime());

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getSpeciesId(), petForm.getBreedId(),
                petForm.getLocation(), petForm.getVaccinated(), petForm.getGender(), petForm.getDescription(),
                birthDate, currentDate, petForm.getPrice(), petForm.getOwner());

        if (!opPet.isPresent()) {
            return uploadPetForm(petForm).addObject("pet_error", true);
        }

        petForm.getPhotos().forEach(photo -> {
            byte[] imgBytes;
            try {
                imgBytes = photo.getBytes();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            imageService.create(opPet.get().getId(), imgBytes, petForm.getOwner());
        });


        return new ModelAndView("redirect:/admi/pet/" + opPet.get().getId());
    }

    @RequestMapping(value = "/admi/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
         petService.removePetAdmin(id);
         LOGGER.debug("Pet {} updated as removed", id);
         return new ModelAndView("redirect:/admi/pets");

    }

    @RequestMapping(value = "/admi/pet/{id}/sell-adopt", method = {RequestMethod.POST})
    public ModelAndView petUpdateSold(@PathVariable("id") long id) {
        petService.sellPetAdmin(id);
        LOGGER.debug("Pet {} updated as sold", id);
        return new ModelAndView("redirect:/admi/pets");
    }

    @RequestMapping(value = "/admi/pet/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView petUpdateRecover(@PathVariable("id") long id) {
        petService.recoverPetAdmin(id);
        LOGGER.debug("Pet {} updated as recovered", id);
        return new ModelAndView("redirect:/admi/pets");
    }


//    USERS ENDPOINTS
    @RequestMapping(value = "/admi/users")
    public ModelAndView getUsersAdmin(@RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "find", required = false) String find) {
        if(page == null){
            page = "1";
        }

        ModelAndView mav = new ModelAndView("admin/admin_users");
        mav.addObject("currentPage", page);

        if(find != null){
            String maxPage = userService.getAdminMaxSearchPages(getLocale(), find);
            mav.addObject("maxPage", maxPage);
            List<User> userList = userService.adminSearchList(getLocale(), find, page);
            mav.addObject("users_list", userList);

        }else{
            String maxPage = userService.getAdminUserPages();
            mav.addObject("maxPage", maxPage);
            List<User> userList = userService.adminUserList(getLocale(), page);
            mav.addObject("users_list", userList);
        }

        return mav;
    }

    @RequestMapping(value = "/admi/user/{id}")
    public ModelAndView getSingleUser(@PathVariable("id") long id,
                                      @RequestParam(name = "page", required = false) String page){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_user");

        if(page == null){
            page = "1";
        }

        mav.addObject("user", userService.findById(getLocale(), id).orElseThrow(UserNotFoundException::new));
        mav.addObject("maxPage", petService.getMaxUserPetsPages(id));
        mav.addObject("currentPage", page);
        mav.addObject("userPets", petService.getByUserId(getLocale(), id, page));

        return mav;
    }


//    REQUESTS ENDPOINTS
    @RequestMapping(value = "/admi/requests")
    public ModelAndView getRequestsAdmin(@RequestParam(name = "status", required = false) String status,
                                         @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                         @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                         @RequestParam(name = "page", required = false) String page,
                                         @RequestParam(name = "find", required = false) String find) {
        if(page == null){
            page = "1";
        }

        final String locale = getLocale();

        ModelAndView mav = new ModelAndView("admin/admin_requests");
        mav.addObject("currentPage", page);


        status = status == null || status.equals("any") ? null : status;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        if ( searchCriteria != null || status != null) {
            String maxPage = requestService.getAdminMaxFilterPages(locale, status);
            mav.addObject("maxPage", maxPage);

            LOGGER.debug("Requesting filtered pet list of parameters: locale: {}, status: {}, sCriteria: {}, sOrder: {}, page: {}",
                    locale, status, searchCriteria, searchOrder, page);
            List<Request> requestList = requestService.adminFilteredList(locale, status, searchCriteria,
                    searchOrder, page);
            mav.addObject("requests_list", requestList);

        }else if(find != null){
            String maxPage = requestService.getAdminMaxSearchPages(locale, find);
            mav.addObject("maxPage", maxPage);
            List<Request> requestList = requestService.adminSearchList(locale, find, page);
            mav.addObject("requests_list", requestList);

        }else{
            String maxPage = requestService.getAdminRequestPages(locale);
            mav.addObject("maxPage", maxPage);
            List<Request> requestList = requestService.adminRequestList(locale, page);
            mav.addObject("requests_list", requestList);
        }

        return mav;
    }

    @RequestMapping(value ="/admi/upload-request", method = { RequestMethod.GET })
    public ModelAndView uploadRequestForm(@ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm) {
        return new ModelAndView("admin/admin_upload_request")
                .addObject("pets_list", petService.listAll(getLocale()))
                .addObject("users_list",userService.list().toArray());
    }

    @RequestMapping(value = "/admi/upload-request", method = { RequestMethod.POST })
    public ModelAndView uploadRequest(@Valid @ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm,
                                      final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadRequestForm(requestForm);
        }

        Optional<Request> optionalRequest = requestService.create(requestForm.getUserId(),requestForm.getPetId(), getLocale());

        if (!optionalRequest.isPresent()) {
            return uploadRequestForm(requestForm).addObject("request_error", true);
        }


        return new ModelAndView("redirect:/admi/requests");
    }

    @RequestMapping(value = "/admi/request/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView requestUpdateCanceled(@PathVariable("id") long id) {
        requestService.cancelRequestAdmin(id);
        LOGGER.debug("Request {} updated as canceled", id);
        return new ModelAndView("redirect:/admi/requests");

    }

    @RequestMapping(value = "/admi/request/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView requestUpdateRecover(@PathVariable("id") long id) {
        requestService.recoverRequestAdmin(id);
        LOGGER.debug("Request {} updated as recovered", id);
        return new ModelAndView("redirect:/admi/requests");
    }

}

