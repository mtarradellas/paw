package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RequestMail;
import ar.edu.itba.paw.webapp.form.ResetPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class LoginAndRegisterController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAndRegisterController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("views/login");
    }

    @RequestMapping("/login/error")
    public ModelAndView failedLogin(){
        ModelAndView mav = new ModelAndView("views/login");
        mav.addObject("loginFailed", true);
        return mav;
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("registerForm") final UserForm userForm) {
        return new ModelAndView("views/register");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                                   final BindingResult errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> LOGGER.debug("{}", error.toString()));
            return registerForm(userForm);
        }

        String locale = getLocale();
        Optional<User> opUser;
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
            opUser = userService.create(userForm.getUsername(), userForm.getPassword(),
                    userForm.getMail(), locale, baseUrl);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return registerForm(userForm)
                    .addObject("duplicatedUsername", ex.getMessage().contains("users_username_key"))
                    .addObject("duplicatedMail", ex.getMessage().contains("users_mail_key"));
        }
        if (opUser == null || !opUser.isPresent()) {
            LOGGER.warn("User creation failed. User returned from creation is {}", opUser==null? "null":"empty");
            return registerForm(userForm).addObject("generalError", true);
        }

        return new ModelAndView("views/email_sent_for_account_activation");
    }

    @RequestMapping(value ="/account-activation", method = { RequestMethod.GET })
    public ModelAndView requestResetPassword(@RequestParam (name = "token", required = true) String tokenString,
                                             HttpServletRequest request) {
        UUID uuid = UUID.fromString(tokenString);

        Optional<User> opUser = userService.activateAccountWithToken(uuid);

        if (!opUser.isPresent()) {
            LOGGER.warn("User could not activate token {}", tokenString);
            return new ModelAndView("views/activation_email_invalid");
        }

        authenticateUserAndSetSession(opUser.get().getUsername(), request);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value ="/request-password-reset", method = { RequestMethod.GET })
    public ModelAndView requestResetPassword(@ModelAttribute ("mailForm") final RequestMail mailForm) {
        return new ModelAndView("views/request_password_reset");
    }

    @RequestMapping(value ="/request-password-reset", method = { RequestMethod.POST })
    public ModelAndView requestResetPassword(@Valid @ModelAttribute ("mailForm") final RequestMail mailForm,
                                             final BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> LOGGER.debug("{}", error.toString()));
            return requestResetPassword(mailForm);
        }

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Optional<User> opUser = userService.requestPasswordReset(mailForm.getMail(), baseUrl);

        if (!opUser.isPresent()) {
            return requestResetPassword(mailForm).addObject("invalidMail", true);
        }
        return new ModelAndView("views/email_sent_for_password_reset");
    }

    @RequestMapping(value ="/password-reset", method = { RequestMethod.GET })
    public ModelAndView resetPassword(@ModelAttribute ("resetPasswordForm") final ResetPasswordForm resetPasswordForm) {
        return new ModelAndView("views/password_reset");
    }

    @RequestMapping(value ="/password-reset", method = { RequestMethod.POST })
    public ModelAndView resetPassword(@Valid @ModelAttribute ("resetPasswordForm")
                                          final ResetPasswordForm resetPasswordForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return resetPassword(resetPasswordForm);
        }
        UUID uuid = UUID.fromString(resetPasswordForm.getToken());
        String password = resetPasswordForm.getPassword();

        Optional<User> opUser = userService.resetPassword(uuid, password);

        if (!opUser.isPresent()) {
            return new ModelAndView("views/token_has_expired");
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value ="/request-link-account", method = { RequestMethod.GET })
    public ModelAndView requestLinkAccount(@ModelAttribute ("mailForm") final RequestMail mailForm) {
        return new ModelAndView("views/request_password_reset");
    }

    @RequestMapping(value ="/request-link-account", method = { RequestMethod.POST })
    public ModelAndView requestLinkAccount(@Valid @ModelAttribute ("mailForm") final RequestMail mailForm,
                                           final BindingResult errors) {
        return requestResetPassword(mailForm, errors);
    }


    @RequestMapping(value = "/403")
    public ModelAndView accessDenied() {
        return new ModelAndView("error-views/403");
    }

    private Authentication authenticateUserAndSetSession(String username, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return authentication;
    }
}

