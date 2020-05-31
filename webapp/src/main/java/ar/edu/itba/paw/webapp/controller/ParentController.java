package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.webapp.auth.PSUserDetailsService;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentController.class);

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
    @Autowired
    LocationService locationService;
    @Autowired
    PSUserDetailsService userDetailsService;


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
        return userService.findByUsername(auth.getName()).orElse(null);
    }

    public Authentication authenticateUserAndSetSession(String username, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return authentication;
    }

    public int parsePage(String page) {
        int pageNum = 1;
        if(page != null) {
            try {
                pageNum = Integer.parseInt(page);
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Invalid page parameter");
            }
        }
        return pageNum;
    }

    public <E extends Enum<E>> E parseStatus(Class<E> enumClass, String statusStr) {
        E status = null;
        E[] values = enumClass.getEnumConstants();
        if(statusStr != null) {
            try {
                int idx = Integer.parseInt(statusStr);
                status = values[idx];
                if (status == null) throw new BadRequestException("Invalid status parameter");
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                throw new BadRequestException("Invalid status parameter");
            }
        }
        return status;
    }

    public Long parseSpecies(String speciesStr) {
        species = species == null || species.equals("any") ? null : species;
        Long species = null;
        if (speciesStr != null && !speciesStr.equalsIgnoreCase("any")) {
            try {
                species = Long.parseLong(speciesStr);
            } catch (NumberFormatException ex) {
                LOGGER.war
            }
        }
        return species;
    }

    public Long parseBreed(String breedStr) {
        breed = breed == null || breed.equals("any") ? null : breed;
    }

    public Long parseProvince(String provinceStr) {

    }

    public Long parseDepartment(String departmentStr) {

    }

    public boolean parseFind(String find) {
        return find == null || find.matches("^[a-zA-Z0-9 \u00C0-\u00D6\u00D8-\u00f6\u00f8-\u00ff-]*$");
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
