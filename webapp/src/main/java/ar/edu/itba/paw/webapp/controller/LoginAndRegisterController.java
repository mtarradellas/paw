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

@Controller
public class LoginAndRegisterController extends ParentController {

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("views/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        final User user = userService.create(userForm.getUsername(), userForm.getPassword(),
                userForm.getMail(), userForm.getPhone());
        final ModelAndView mav = new ModelAndView("redirect: /user/" + user.getId());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("registerForm") final UserForm userForm) {
        return new ModelAndView("views/register");
    }

    @RequestMapping(value = "/403")
    public ModelAndView accessDenied(){
        return new ModelAndView("error-views/403");
    }
}
