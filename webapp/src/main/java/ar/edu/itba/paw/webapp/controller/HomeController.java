package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;

    @RequestMapping("/")
    public ModelAndView getHome() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("home_pet_list", petService.list());
        return mav;
    }

    @RequestMapping("/about")
    public ModelAndView getAbout() {
        final ModelAndView mav = new ModelAndView("about");
        return mav;
    }
}
