package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends ParentController {

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available")
                    .addObject("species_list", speciesService.speciesList(getLocale()).toArray())
                    .addObject("breeds_list", speciesService.breedsList(getLocale()).toArray());
    }

    @RequestMapping("/contact")
    public ModelAndView getContact() {
        return new ModelAndView("views/contact");
    }

    @RequestMapping(value = "/test")
    public ModelAndView getIdPet() {
        final ModelAndView mav = new ModelAndView("views/test");
        return mav;
    }
}
