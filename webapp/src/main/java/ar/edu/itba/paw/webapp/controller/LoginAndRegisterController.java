package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginAndRegisterController extends ParentController {

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("views/login");
    }

    /* TODO redirect to home already logged in*/
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        Optional<User> opUser;
        try {
            opUser = userService.create(userForm.getUsername(), userForm.getPassword(),
                    userForm.getMail(), userForm.getPhone());
        } catch (RuntimeException ex) {
            return registerForm(userForm).addObject("invalidUser", true);
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("registerForm") final UserForm userForm) {
        return new ModelAndView("views/register");
    }
}
