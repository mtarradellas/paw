package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_user");
        mav.addObject("user",
                userService.findById(id).orElseThrow(UserNotFoundException::new));
        LOGGER.debug("Loading user {} page", id);
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests() {
        final ModelAndView mav = new ModelAndView("views/requests");
//        mav.addObject("user",
//                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested() {
        final ModelAndView mav = new ModelAndView("views/interests");
//        mav.addObject("user",
//                userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }
}