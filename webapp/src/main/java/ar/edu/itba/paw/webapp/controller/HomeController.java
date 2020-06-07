package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.LocationService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends ParentController {
    @Autowired
    private SpeciesService speciesService;
    @Autowired
    private PetService petService;

    @Autowired
    private LocationService locationService;

    @RequestMapping("/maps")
    public ModelAndView getMaps() {
        return new ModelAndView("views/maps");
    }

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        final String locale = getLocale();
        return new ModelAndView("views/available")
                    .addObject("speciesList", speciesService.speciesList(locale).toArray())
                    .addObject("breedList", speciesService.breedList(locale).toArray());
    }

    @RequestMapping(value = "/admin")
    public ModelAndView getAdminHome() {
        return new ModelAndView("admin/admin");
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        final String locale = getLocale();
        return new ModelAndView("views/test")
                .addObject("pets", petService.filteredList(getLocale(),null,null,
                        null, null, null, null,null,null,0,
                        0,null,null,0,0));

    }
}
