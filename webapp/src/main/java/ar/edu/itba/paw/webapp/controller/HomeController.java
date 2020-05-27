package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.DepartmentList;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends ParentController {

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available")
                    .addObject("species_list", speciesService.speciesList().toArray())
                    .addObject("breeds_list", speciesService.breedsList().toArray());
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("views/test")
                .addObject("species", speciesService.findSpeciesById(1).orElseThrow(UserNotFoundException::new))
                .addObject("breed", speciesService.findBreedById(1).orElseThrow(UserNotFoundException::new));
    }
}
