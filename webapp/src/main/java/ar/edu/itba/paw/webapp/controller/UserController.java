package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_user");
        mav.addObject("user",
                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    // TODO Add "user/{id}" & @PathVariable("id") long id
    @RequestMapping(value = "/requests")
    public ModelAndView getRequests() {
        final ModelAndView mav = new ModelAndView("views/requests");
//        mav.addObject("user",
//                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    // TODO Add "user/{id}" & @PathVariable("id") long id
    @RequestMapping(value = "/interests")
    public ModelAndView getInterested() {
        final ModelAndView mav = new ModelAndView("views/interests");
//        mav.addObject("user",
//                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error-views/404_user");
    }

}
