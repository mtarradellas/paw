package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    ImageService imageService;
    @Autowired
    SpeciesService speciesService;
    @Autowired
    RequestService requestService;

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available");
    }

    @RequestMapping("/contact")
    public ModelAndView getContact() {
        return new ModelAndView("views/contact");
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        mav.addObject("pet",
                petService.findById(getLocale(),id).orElseThrow(PetNotFoundException::new));
        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");

        mav.addObject("request",
                requestService.create(6,1,getLocale()).get());
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests() {
        final ModelAndView mav = new ModelAndView("views/requests");
        return mav;
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested() {
        final ModelAndView mav = new ModelAndView("views/interests");
        return mav;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                 @RequestParam(name = "breed", required = false) String breed,
                                 @RequestParam(name = "gender", required = false) String gender,
                                 @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                 @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                 @RequestParam(name = "find", required = false) String findValue){

        final ModelAndView mav = new ModelAndView("index");

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        if(species != null || gender != null || searchCriteria != null){
            mav.addObject("home_pet_list", petService.filteredList(getLocale(), species, breed, gender, searchCriteria, searchOrder));
        }
        else if(findValue != null){
            mav.addObject("home_pet_list", petService.find(getLocale(),findValue).toArray());
        }
        else {

            mav.addObject("home_pet_list", petService.list(getLocale()));
        }
        mav.addObject("species_list", speciesService.speciesList(getLocale()).toArray());
        mav.addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
        return mav;
    }

    private String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error-views/404_user");
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("error-views/404_pet");
    }

}
