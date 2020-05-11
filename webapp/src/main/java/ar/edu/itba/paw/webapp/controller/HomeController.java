package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.DepartmentList;
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
        DepartmentList departmentList = locationService.departmentList();
        return new ModelAndView("views/contact").addObject("provinces", departmentList.getProvinceList());
    }

}
