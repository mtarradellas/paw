package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class ParentController {

    @Autowired
    SpeciesService speciesService;
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    ImageService imageService;
    @Autowired
    MailService mailService;
    @Autowired
    RequestService requestService;

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
        return userService.findByUsername(getLocale(), auth.getName()).orElse(null);
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

}
