package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Supplier;

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
        return new ModelAndView("about");
    }

    @RequestMapping("/users")
    public ModelAndView getUsers() {
        final ModelAndView mav = new ModelAndView("users");
        mav.addObject("users_list", userService.list().toArray());
        return mav;
    }

    @RequestMapping(value = "/{id}")
    public ModelAndView getIdUser(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("single_user");
        mav.addObject("single_user_example",
                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("404");
    }

}
