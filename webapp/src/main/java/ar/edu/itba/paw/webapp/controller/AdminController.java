package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController extends ParentController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    //TODO: cambiar el path del mapping para que te redireccion correctamente
    @RequestMapping(value = "/admi")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }

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

}

