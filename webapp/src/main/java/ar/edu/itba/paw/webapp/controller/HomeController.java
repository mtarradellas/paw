package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.interfaces.UserService;
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
    SpeciesService speciesService;
    @Autowired
    PetService petService;

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available");
    }

    @RequestMapping("/contact")
    public ModelAndView getContact() {
        return new ModelAndView("views/contact");
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");

        mav.addObject("pet_list",
                petService.list("en_US"));
        return mav;
    }

}
