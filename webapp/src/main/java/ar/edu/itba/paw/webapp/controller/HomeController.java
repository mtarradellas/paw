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
                .addObject("provinceList", locationService.provinceList().toArray());

    }
}
