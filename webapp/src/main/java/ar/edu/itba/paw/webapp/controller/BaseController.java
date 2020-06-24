package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;

    protected String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

    @ModelAttribute("loggedUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            return null;
        }
        Optional<User> opUser = userService.findByUsername(auth.getName());
        if (opUser.isPresent()) {
            User user = opUser.get();
            String locale = getLocale();
            if (user.getLocale() == null || !user.getLocale().equalsIgnoreCase(locale)) {
                userService.updateLocale(user, locale);
            }
            return opUser.get();
        }
        return null;
    }

    @ModelAttribute("interestNotif")
    public int interestNotif() {
        User user = loggedUser();
        int interestNotif = 0;
        if (user != null) {
            interestNotif = requestService.interestNotifs(user);
        }
        return interestNotif;
    }

    @ModelAttribute("requestNotif")
    public int requestNotif() {
        User user = loggedUser();
        int requestNotif = 0;
        if (user != null) {
            requestNotif = requestService.requestNotifs(user);
        }
        return requestNotif;
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("error-views/404_pet");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error-views/404_user");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest() {
        return new ModelAndView("error-views/400");
    }

}
