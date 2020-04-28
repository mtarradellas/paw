package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PSUserDetailsService;
import ar.edu.itba.paw.webapp.form.RequestMail;
import ar.edu.itba.paw.webapp.form.ResetPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class LoginAndRegisterController extends ParentController {

    @Autowired
    private PSUserDetailsService userDetailsService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("views/login");
    }

    @RequestMapping(value ="/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("registerForm") final UserForm userForm) {
        return new ModelAndView("views/register");
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
        } catch (DuplicateUserException ex) {
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

    @RequestMapping(value ="/request-password-reset", method = { RequestMethod.GET })
    public ModelAndView requestResetPassword(@ModelAttribute ("mailForm") final RequestMail mailForm) {
        return new ModelAndView("views/request_password_reset");
    }

    @RequestMapping(value ="/request-password-reset", method = { RequestMethod.POST })
    public ModelAndView requestResetPassword(@Valid @ModelAttribute ("mailForm") final RequestMail mailForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return requestResetPassword(mailForm);
        }
        Optional<User> opUser = userService.findByMail(mailForm.getMail());
        if(opUser.isPresent()){

            UUID uuid = UUID.randomUUID();
            //chequear el return
            userService.createToken(uuid, opUser.get().getId());
            mailService.sendMail(opUser.get().getMail(),resetPasswordSubject(),resetPasswordBody(opUser.get(),uuid));
            
        }
        return requestResetPassword(mailForm)
                .addObject("invalid_mail", true);
    }

    @RequestMapping(value ="/password-reset", method = { RequestMethod.GET })
    public ModelAndView resetPassword(@ModelAttribute ("resetPasswordForm") final ResetPasswordForm resetPasswordForm) {
        return new ModelAndView("views/password_reset");
    }

    @RequestMapping(value ="/password-reset", method = { RequestMethod.POST })
    public ModelAndView resetPassword(@ModelAttribute ("resetPasswordForm") final ResetPasswordForm resetPasswordForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return resetPassword(resetPasswordForm);
        }
        userService.updatePassword(resetPasswordForm.getPassword());
        return new ModelAndView("redirect:/login");
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

    private String resetPasswordBody(User user, UUID uuid) {
        String url = "http://pawserver.it.itba.edu.ar/paw-2020a-7/password-reset";
        url += "?token=" + uuid;
        String body;
        if(getLocale().equals("en_US")) {
            body = "Hello " + user.getUsername() +
                    ",\nPlease click the link below to reset your password\n"
                    + url +
                    "\nSincerely,\nPet Society Team.";
        }
        else{
            body = "Hola " + user.getUsername() +
                    ",\nPor favor haz click en el siguiente link para resetear tu contraseña\n"
                    + url +
                    "\nSinceramente,\nEl equipo de Pet Society.";
        }
        return body;
    }
    private String resetPasswordSubject() {
        String subject;
        if(getLocale().equals("en_US")) {
            subject = "Reset Your Password";
        }
        else { subject = "Resetea tu contraseña"; }
        return subject;
    }
}
