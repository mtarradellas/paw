package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exception.ImageLoadException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AdminUploadPetForm;
import ar.edu.itba.paw.webapp.form.AdminUploadRequestForm;
import ar.edu.itba.paw.webapp.form.UploadPetForm;
import ar.edu.itba.paw.webapp.form.UserForm;
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
import java.util.UUID;

@Controller
public class AdminController extends ParentController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    @RequestMapping(value = "/admin")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }



//    PETS ENDPOINTS
    @RequestMapping(value = "/admin/pets")
    public ModelAndView getPetsAdmin(@RequestParam(name = "species", required = false) String species,
                                     @RequestParam(name = "breed", required = false) String breed,
                                     @RequestParam(name = "gender", required = false) String gender,
                                     @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                     @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                     @RequestParam(name = "find", required = false) String findValue,
                                     @RequestParam(name = "page", required = false) String page,
                                     @RequestParam(name = "minPrice", required = false) String minPrice,
                                     @RequestParam(name = "maxPrice", required = false) String maxPrice,
                                     @RequestParam(name = "status", required = false) String status) {

        ModelAndView mav = new ModelAndView("admin/admin_pets");
        final String locale = getLocale();

        if(page == null){
            page = "1";
        }

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        status = status == null || status.equals("any") ? null : status;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        PetList petList = petService.adminPetList(locale, findValue, species, breed, gender, status, searchCriteria,
                                                    searchOrder, minPrice, maxPrice, page);

        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petList.getMaxPage());
        mav.addObject("pets_list", petList);
        mav.addObject("species_list", petList.getSpecies());
        mav.addObject("breeds_list", petList.getBreeds());

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

        BreedList breedList = speciesService.breedsList(locale);
        mav.addObject("species_list", breedList.getSpecies().toArray());
        mav.addObject("breeds_list", breedList.toArray());
        mav.addObject("users_list", userService.list(locale).toArray());
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
            return uploadPetForm(petForm).addObject("image_error", true);
        }

        Optional<Pet> opPet = petService.create(getLocale(), petForm.getPetName(), petForm.getSpeciesId(), petForm.getBreedId(),
                petForm.getLocation(), petForm.getVaccinated(), petForm.getGender(), petForm.getDescription(),
                birthDate, currentDate, petForm.getPrice(), petForm.getOwner(), 15, photos);

        if (!opPet.isPresent()) {
            LOGGER.warn("Pet could not be created");
            return uploadPetForm(petForm).addObject("pet_error", true);
        }


        return new ModelAndView("redirect:/admin/pet/" + opPet.get().getId());
    }

    @RequestMapping(value = "/admin/pet/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView petUpdateRemoved(@PathVariable("id") long id) {
         petService.removePetAdmin(id);
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


//    USERS ENDPOINTS
@RequestMapping(value = "/admin/users")
public ModelAndView getUsersAdmin(@RequestParam(name = "status", required = false) String status,
                                  @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                  @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                  @RequestParam(name = "page", required = false) String page,
                                  @RequestParam(name = "find", required = false) String findValue) {

    ModelAndView mav = new ModelAndView("admin/admin_users");
    final String locale = getLocale();

    if(page == null){
        page = "1";
    }

    status = status == null || status.equals("any") ? null : status;
    searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

    UserList userList = userService.adminUserList(locale, findValue, status, searchCriteria, searchOrder, page);

    mav.addObject("currentPage", page);
    mav.addObject("maxPage", userList.getMaxPage());
    mav.addObject("users_list", userList);

    return mav;
}

    @RequestMapping(value = "/admin/user/{id}")
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


    @RequestMapping(value ="/admin/upload-user", method = { RequestMethod.GET })
    public ModelAndView uploadUserForm(@ModelAttribute("registerForm") final UserForm userForm) {
        return new ModelAndView("admin/admin_upload_user");
    }

    @RequestMapping(value = "/admin/upload-user", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                                   final BindingResult errors, HttpServletRequest request) {

        final String locale = getLocale();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> LOGGER.debug("{}", error.toString()));
            return uploadUserForm(userForm);
        }

        Optional<User> opUser;
        try {
            opUser = userService.adminCreate(locale, userForm.getUsername(), userForm.getPassword(),
                    userForm.getMail(), userForm.getPhone());
        } catch (DuplicateUserException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return uploadUserForm(userForm)
                    .addObject("duplicatedUsername", ex.isDuplicatedUsername())
                    .addObject("duplicatedMail", ex.isDuplicatedMail());
        }
        if (opUser == null || !opUser.isPresent()) {
            LOGGER.warn("User creation failed. User returned from creation is {}", opUser==null? "null":"empty");
            return uploadUserForm(userForm).addObject("generalError", true);
        }

        return new ModelAndView("redirect:/admin/users");
    }


    @RequestMapping(value = "/admin/user/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView userUpdateDelete(@PathVariable("id") long id) {
        userService.removeAdmin(id);
        LOGGER.debug("User {} updated as deleted", id);
        return new ModelAndView("redirect:/admin/users");

    }

    @RequestMapping(value = "/admin/user/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView userUpdateRecover(@PathVariable("id") long id) {
        userService.recoverAdmin(id);
        LOGGER.debug("User {} updated as recovered", id);
        return new ModelAndView("redirect:/admin/users");
    }




//    REQUESTS ENDPOINTS
    @RequestMapping(value = "/admin/requests")
    public ModelAndView getRequestsAdmin(@RequestParam(name = "status", required = false) String status,
                                         @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                         @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                         @RequestParam(name = "page", required = false) String page,
                                         @RequestParam(name = "find", required = false) String findValue) {

        ModelAndView mav = new ModelAndView("admin/admin_requests");
        final String locale = getLocale();

        if(page == null){
            page = "1";
        }

        status = status == null || status.equals("any") ? null : status;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        RequestList requestList = requestService.adminRequestList(locale, findValue, status, searchCriteria, searchOrder, page);

        mav.addObject("currentPage", page);
        mav.addObject("maxPage", requestList.getMaxPage());
        mav.addObject("requests_list", requestList);

        return mav;
    }

    @RequestMapping(value ="/admin/upload-request", method = { RequestMethod.GET })
    public ModelAndView uploadRequestForm(@ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm) {
        String language = getLocale();
        return new ModelAndView("admin/admin_upload_request")
                .addObject("pets_list", petService.listAll(language))
                .addObject("users_list",userService.list(language).toArray());
    }

    @RequestMapping(value = "/admin/upload-request", method = { RequestMethod.POST })
    public ModelAndView uploadRequest(@Valid @ModelAttribute("adminUploadRequestForm") final AdminUploadRequestForm requestForm,
                                      final BindingResult errors, HttpServletRequest request) {


        if (errors.hasErrors()) {
            return uploadRequestForm(requestForm);
        }

        Optional<Request> optionalRequest = requestService.create(requestForm.getUserId(),requestForm.getPetId(), getLocale());

        if (!optionalRequest.isPresent()) {
            return uploadRequestForm(requestForm).addObject("request_error", true);
        }


        return new ModelAndView("redirect:/admin/requests");
    }

    @RequestMapping(value = "/admin/request/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView requestUpdateCanceled(@PathVariable("id") long id) {
        requestService.cancelRequestAdmin(id);
        LOGGER.debug("Request {} updated as canceled", id);
        return new ModelAndView("redirect:/admin/requests");

    }

    @RequestMapping(value = "/admin/request/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView requestUpdateRecover(@PathVariable("id") long id) {
        requestService.recoverRequestAdmin(id);
        LOGGER.debug("Request {} updated as recovered", id);
        return new ModelAndView("redirect:/admin/requests");
    }

    @RequestMapping(value ="/admin/request/{id}/edit", method = { RequestMethod.GET })
    public ModelAndView editRequest(@PathVariable("id") long id) {

        Optional<Request> request = requestService.findById(id,getLocale());
        if(!request.isPresent()){
            return new ModelAndView("error-views/404");
        }

        return new ModelAndView("admin/admin_edit_request")
                .addObject("request", request.get());
    }

    @RequestMapping(value = "/admin/request/{id}/edit", method = { RequestMethod.POST })
    public ModelAndView uploadRequest(@PathVariable("id") long id,
                                      @RequestParam(name = "newStatus", required = false) String newStatus) {

        requestService.adminUpdateStatus(id,newStatus);

        return new ModelAndView("redirect:/admin/requests");
    }

}

