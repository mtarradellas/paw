package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @RequestMapping("/")
    public ModelAndView getHome() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("home_pet_list", userService.list());
        return mav;
    }
}
