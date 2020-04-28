package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exception.InvalidUserCreationException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PSUserDetailsService;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class LoginAndRegisterController extends ParentController {

    @Autowired
    private PSUserDetailsService userDetailsService;

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("views/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                                   final BindingResult errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return registerForm(userForm);
        }

        Optional<User> opUser;
        try {
            opUser = userService.create(userForm.getUsername(), userForm.getPassword(),
                    userForm.getMail(), userForm.getPhone());
        } catch (InvalidUserCreationException ex) {
            return registerForm(userForm)
                    .addObject("duplicatedUsername", ex.isDuplicatedUsername())
                    .addObject("duplicatedMail", ex.isDuplicatedMail());
        }

        if (opUser == null || !opUser.isPresent()) {
            return registerForm(userForm).addObject("generalError", true);
        }

        authenticateUserAndSetSession(opUser.get().getUsername(), request);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("registerForm") final UserForm userForm) {
        return new ModelAndView("views/register");
    }

    public Authentication authenticateUserAndSetSession(String username,HttpServletRequest request){

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return authentication;
    }

    @RequestMapping(value = "/403")
    public ModelAndView accessDenied() {
        return new ModelAndView("error-views/403");
    }
}
