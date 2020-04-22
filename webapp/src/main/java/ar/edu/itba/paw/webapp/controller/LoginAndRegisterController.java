package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginAndRegisterController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ModelAndView login(@Valid @ModelAttribute("form") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return loginForm(userForm);
        }

        Optional<User> opUser = userService.findByUsername(userForm.getUsername());
        if (!opUser.isPresent()) {
            return loginForm(userForm);
        }
        final ModelAndView mav = new ModelAndView("views/single_user");
        mav.addObject("user", opUser.get());
        return mav;
    }

    @RequestMapping(value ="/login", method = { RequestMethod.GET })
    public ModelAndView loginForm(@ModelAttribute ("form") final UserForm userForm) {
        return new ModelAndView("views/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("form") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        final User user = userService.create(userForm.getUsername(), userForm.getPassword(),
                userForm.getMail(), userForm.getPhone());
        final ModelAndView mav = new ModelAndView("views/single_user");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("form") final UserForm userForm) {
        return new ModelAndView("views/register");
    }
}
