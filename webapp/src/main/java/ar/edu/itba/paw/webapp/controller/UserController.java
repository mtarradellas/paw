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
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);
        final ModelAndView mav = new ModelAndView("views/requests");

        if(status != null || searchCriteria != null) {
            //hardcodeado con user 1 porque no se de donde sacar el ownerid (user logeado)
            mav.addObject("requests_list",
                    requestService.filterListByOwner(getLocale(), 1, status, searchCriteria,searchOrder).toArray());
        }
        else{
            //hardcodeado con user 1 porque no se de donde sacar el ownerid (user logeado)
            mav.addObject("requests_list",
                    requestService.listByOwner(getLocale(),1).toArray());
        }

        return mav;
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);
        final ModelAndView mav = new ModelAndView("views/interests");

        System.out.println();

        if(status != null || searchCriteria != null) {
            mav.addObject("interests_list",
                    requestService.filterListByPetOwner(getLocale(), 1, status, searchCriteria, searchOrder).toArray());
        }
        else{
            mav.addObject("interests_list",
                    requestService.listByPetOwner(getLocale(),1).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/interests-accept-reject/{id}", method = {RequestMethod.POST})
    public ModelAndView changeStatus(@RequestParam(name = "newStatus", required = false) String status,
                                     @PathVariable("id") long id) {


            if(status.equals("accept")){
                requestService.updateStatus(id,"accepted",getLocale());

            }else if (status.equals("reject")){
                requestService.updateStatus(id,"rejected",getLocale());
            }

        return getInterested(null,null,null);

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
