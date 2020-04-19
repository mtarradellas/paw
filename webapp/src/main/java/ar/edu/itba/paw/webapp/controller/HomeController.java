package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.apache.taglibs.standard.lang.jstl.NullLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    ImageService imageService;

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available");
    }

    @RequestMapping("/contact")
    public ModelAndView getContact() {
        final ModelAndView mav = new ModelAndView("views/contact");
        return mav;
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        mav.addObject("pet",
                petService.findById(id).orElseThrow(PetNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");
        mav.addObject("pet",
                petService.findById(1).orElseThrow(PetNotFoundException::new));
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
            mav.addObject("home_pet_list", petService.filteredList(species, breed, gender, searchCriteria, searchOrder));
        }
        else if(findValue != null){
            mav.addObject("home_pet_list", petService.find(findValue).toArray());
        }
        else {

            mav.addObject("home_pet_list", petService.list());
        }
        return mav;
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("views/404_user");
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("views/404_pet");
    }
}
