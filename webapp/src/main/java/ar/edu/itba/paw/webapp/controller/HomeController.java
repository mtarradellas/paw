package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.LocationService;
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
    private RequestService requestService;

    @Autowired
    private LocationService locationService;

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
                .addObject("requests", requestService.filteredList(null,null,"nico",null,null, null, 0, 0).toArray());

    }
}
