package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.UserStatus;
import ar.edu.itba.paw.webapp.controller.ParentController;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditUserForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.groups.BasicInfoEditUser;
import ar.edu.itba.paw.webapp.form.groups.ChangePasswordEditUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminUserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final int USER_PAGE_SIZE = 25;
    private static final int PET_PAGE_SIZE = 25;

    @RequestMapping(value = "/admin/users")
    public ModelAndView getUsersAdmin(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                      @RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "find", required = false) String find) {

        ModelAndView mav = new ModelAndView("admin/admin_users");

        int pageNum = parsePage(page);
        UserStatus userStatus = parseStatus(UserStatus.class, status);
        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);

        if (!isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = parseFind(find);

        List<User> userList = userService.filteredList(findList, userStatus, searchCriteria, searchOrder, pageNum, USER_PAGE_SIZE);
        int amount = userService.getFilteredAmount(findList, userStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / USER_PAGE_SIZE));
        mav.addObject("userList", userList);
        mav.addObject("amount", amount);

        return mav;
    }

    @RequestMapping(value = "/admin/user/{id}")
    public ModelAndView getSingleUser(@PathVariable("id") long id,
                                      @RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "showAllReviews", required = false) String showAllReviews){

        final ModelAndView mav = new ModelAndView("/admin/admin_single_user");
        String locale = getLocale();

        if(showAllReviews == null || (!showAllReviews.equals("true") && !showAllReviews.equals("false"))){
            showAllReviews = "false";
        }

        int pageNum = parsePage(page);

        List<Pet> petList = petService.listByUser(locale, id, pageNum, PET_PAGE_SIZE);
        int amount = petService.getListByUserAmount(locale, id);
        Optional<User> opUser = userService.findById(id);

        if (!opUser.isPresent()) throw new UserNotFoundException("User " + id + " not found");
        User user = opUser.get();

        mav.addObject("showAllReviews", showAllReviews);
        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
        mav.addObject("userPets", petList);
        mav.addObject("amount", amount);
        mav.addObject("user", user);
        return mav;
    }


    @RequestMapping(value ="/admin/upload-user", method = { RequestMethod.GET })
    public ModelAndView uploadUserForm(@ModelAttribute("registerForm") final UserForm userForm) {
        return new ModelAndView("admin/admin_upload_user");
    }

    @RequestMapping(value = "/admin/upload-user", method = { RequestMethod.POST })
    public ModelAndView createUser(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                                   final BindingResult errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> LOGGER.debug("{}", error.toString()));
            return uploadUserForm(userForm);
        }

        String locale = getLocale();
        Optional<User> opUser;
        try {
            opUser = userService.adminCreate(userForm.getUsername(), userForm.getPassword(),
                    userForm.getMail(), locale);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return uploadUserForm(userForm)
                    .addObject("duplicatedUsername", ex.getMessage().contains("users_username_key"))
                    .addObject("duplicatedMail", ex.getMessage().contains("users_mail_key"));
        }

        if (opUser == null || !opUser.isPresent()) {
            LOGGER.warn("User creation failed. User returned from creation is {}", opUser==null? "null":"empty");
            return uploadUserForm(userForm).addObject("generalError", true);
        }

        return new ModelAndView("redirect:/admin/users");
    }


    @RequestMapping(value = "/admin/user/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView userUpdateDelete(@PathVariable("id") long id) {
        userService.removeUser(id);
        LOGGER.debug("User {} updated as deleted", id);
        return new ModelAndView("redirect:/admin/users");

    }

    @RequestMapping(value = "/admin/user/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView userUpdateRecover(@PathVariable("id") long id) {
        userService.recoverUser(id);
        LOGGER.debug("User {} updated as recovered", id);
        return new ModelAndView("redirect:/admin/users");
    }

    @RequestMapping(value = "/admin/user/{id}/edit", method = { RequestMethod.GET })
    public ModelAndView editUserGet(@ModelAttribute("editUserForm") final EditUserForm editUserForm, @PathVariable("id") long id){

        return editUserForm(populateForm(editUserForm, id), id);
    }


    private EditUserForm populateForm(EditUserForm editUserForm, long id){

        User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        editUserForm.setUsername(user.getUsername());

        return editUserForm;
    }

    private ModelAndView editUserForm(@ModelAttribute("editUserForm") final EditUserForm editUserForm, long id) {

        return new ModelAndView("admin/admin_edit_user")
                .addObject("user",
                        userService.findById(id).orElseThrow(UserNotFoundException::new))
                .addObject("id", id);
    }

    @RequestMapping(value = "/admin/user/{id}/edit", method = { RequestMethod.POST }, params={"update-basic-info"})
    public ModelAndView editBasicInfo(@Validated({BasicInfoEditUser.class}) @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                      final BindingResult errors, HttpServletRequest request,
                                      @PathVariable("id") long id) {

        long adminId = loggedUser().getId();
        if (errors.hasErrors()) {
            return editUserForm(editUserForm, id);
        }
        Optional<User> opUser;
        try {
            opUser = userService.updateUsername(id, editUserForm.getUsername());
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return editUserForm(editUserForm, id)
                    .addObject("duplicatedUsername", ex.getMessage().contains("users_username_key"));
        }
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/error-views/500");
        }

        if(id == adminId){
            authenticateUserAndSetSession(opUser.get().getUsername(),request);
        }
        return new ModelAndView("redirect:/admin/user/" + opUser.get().getId());
    }

    @RequestMapping(value = "/admin/user/{id}/edit", method = { RequestMethod.POST }, params={"update-password"})
    public ModelAndView editPassword(@Validated({ChangePasswordEditUser.class}) @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                     final BindingResult errors, HttpServletRequest request,
                                     @PathVariable("id") long id) {

        if (errors.hasErrors()) {
            populateForm(editUserForm, id);
            return editUserForm(editUserForm, id);
        }
        Optional<User> opUser;
        try {
            opUser = userService.updatePassword(id, editUserForm.getCurrentPassword(), editUserForm.getNewPassword());
        }
        catch(DataIntegrityViolationException ex) {
            return editUserForm(editUserForm, id).addObject("currentPasswordFail", true);
        }
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/error-views/500");
        }
        return new ModelAndView("redirect:/admin/user/" + opUser.get().getId());

    }

    private Authentication authenticateUserAndSetSession(String username, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return authentication;
    }
}
