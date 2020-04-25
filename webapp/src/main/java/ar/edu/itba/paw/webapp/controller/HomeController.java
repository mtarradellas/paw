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

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");
        mav.addObject("request",
                requestService.create(6,2,getLocale()).get());
        return mav;
    }

    protected String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }
}
