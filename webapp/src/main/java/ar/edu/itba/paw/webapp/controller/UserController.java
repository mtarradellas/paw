package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;
    @Autowired
    MailService mailService;

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
            mav.addObject("requests", requestService.listByPetOwner(getLocale(),1));
            return mav;
        }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested() {
        final ModelAndView mav = new ModelAndView("views/interests");
        mav.addObject("requests", requestService.listByPetOwner(getLocale(),1).toArray());

        return mav;
    }

    @RequestMapping(value = "/interests-accept-reject/{id}", method = {RequestMethod.POST})
    public ModelAndView changeStatus(@RequestParam(name = "status", required = false) String status,
                                     @PathVariable("id") long id) {


            if(status.equals("accept")){
                requestService.updateStatus(id,"accepted",getLocale());

            }else if (status.equals("reject")){
                requestService.updateStatus(id,"rejected",getLocale());
            }

        return getInterested();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error-views/404_user");
    }

    protected String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

}
