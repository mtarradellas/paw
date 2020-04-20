package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.PetNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.apache.taglibs.standard.lang.jstl.NullLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Locale;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    ImageService imageService;

    @RequestMapping("/available")
    public ModelAndView getAvailable() {
        return new ModelAndView("views/available");
    }

    @RequestMapping("/contact")
    public ModelAndView getContact() {
        final ModelAndView mav = new ModelAndView("views/contact");
        return mav;
    }

    @RequestMapping(value = "/pet/{id}")
    public ModelAndView getIdPet(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("views/single_pet");
        mav.addObject("pet",
                petService.findById(id).orElseThrow(PetNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET})
    public ModelAndView getHome(@RequestParam(name = "species", required = false) String species,
                                 @RequestParam(name = "breed", required = false) String breed,
                                 @RequestParam(name = "gender", required = false) String gender,
                                 @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                 @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                 @RequestParam(name = "find", required = false) String findValue){

        final ModelAndView mav = new ModelAndView("index");

        species = species == null || species.equals("any") ? null : species;
        breed = breed == null || breed.equals("any") ? null : breed;
        gender = gender == null || gender.equals("any") ? null : gender;
        searchCriteria = searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria;

        if(species != null || gender != null || searchCriteria != null){
            mav.addObject("home_pet_list", petService.filteredList(species, breed, gender, searchCriteria, searchOrder));
        }
        else if(findValue != null){
            mav.addObject("home_pet_list", petService.find(findValue).toArray());
        }
        else {

            mav.addObject("home_pet_list", petService.list());
        }
        return mav;
    }

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("form") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        final User user = userService.create(userForm.getUsername(), userForm.getPassword(),
                        userForm.getMail(), userForm.getPhone());
        final ModelAndView mav = new ModelAndView("views/user");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value ="/create", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("form") final UserForm userForm) {
        return new ModelAndView("views/userform");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("views/404_user");
    }

    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchPet() {
        return new ModelAndView("views/404_pet");
    }
}
