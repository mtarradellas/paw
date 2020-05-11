package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditUserForm;
import ar.edu.itba.paw.webapp.form.groups.BasicInfoEditUser;
import ar.edu.itba.paw.webapp.form.groups.ChangePasswordEditUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    String  page = "1";

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page) {

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();

        if (page == null){
            page = "1";
        }
        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petService.getMaxUserPetsPages(id));
        Optional<User> opUser = userService.findById(locale, id);
        if (!opUser.isPresent()) throw new UserNotFoundException("User " + id + " not found");
        mav.addObject("user", opUser.get());
        mav.addObject("userPets", petService.getByUserId(locale, id, page));
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        final ModelAndView mav = new ModelAndView("views/requests");
        final String locale = getLocale();
        final User user = loggedUser();

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered request list */
        if (status != null || searchCriteria != null) {
            mav.addObject("requests_list",
                    requestService.filterListByOwner(locale, user.getId(), status, searchCriteria, searchOrder).toArray());
        }
        /* Default request list */
        else {
            mav.addObject("requests_list",
                    requestService.listByOwner(locale, user.getId()).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/requests/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.cancel(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder) {

        final ModelAndView mav = new ModelAndView("views/interests");
        final User user = loggedUser();
        final String locale = getLocale();

        status = (status == null || status.equals("any") ? null : status);
        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered interest list */
        if(status != null || searchCriteria != null) {
            mav.addObject("interests_list",
                    requestService.filterListByPetOwner(locale, user.getId(), status, searchCriteria, searchOrder).toArray());
        }
        /* Default interest list */
        else{
            mav.addObject("interests_list",
                    requestService.listByPetOwner(locale, user.getId()).toArray());
        }
        return mav;
    }

    @RequestMapping(value = "/interests/{id}/accept", method = {RequestMethod.POST})
    public ModelAndView acceptInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.accept(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests/{id}/reject", method = {RequestMethod.POST})
    public ModelAndView rejectInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.reject(id, user.getId(), locale)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.GET })
    public ModelAndView editUserGet(@ModelAttribute("editUserForm") final EditUserForm editUserForm, @PathVariable("id") long id){
        final String locale = getLocale();

        if(loggedUser().getId() != id) {
            return new ModelAndView("redirect:/403" );
        }
        User user = userService.findById(locale, id).orElseThrow(UserNotFoundException::new);

        editUserForm.setPhone(user.getPhone());
        editUserForm.setUsername(user.getUsername());

        return editUserForm(editUserForm, id);
    }

    private ModelAndView editUserForm(@ModelAttribute("editUserForm") final EditUserForm editUserForm, long id) {
        final String locale = getLocale();

        return new ModelAndView("views/user_edit")
                .addObject("user",
                        userService.findById(locale, id).orElseThrow(UserNotFoundException::new))
                .addObject("id", id);
    }

    @RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.POST }, params={"update-basic-info"})
    public ModelAndView editBasicInfo(@Validated({BasicInfoEditUser.class}) @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                final BindingResult errors, HttpServletRequest request,
                                @PathVariable("id") long id) {

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n BASIC INFO \n\n\n\n\n\n\n\n\n\n\n\n");

        if (errors.hasErrors()) {
            return editUserForm(editUserForm, id);
        }
        if(loggedUser().getId() != id) {
            return new ModelAndView("redirect:/403");
        }
        Optional<User> opUser;
        try {
             opUser = userService.update(getLocale(), id, editUserForm.getUsername(), editUserForm.getPhone());
        } catch (DuplicateUserException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return editUserForm(editUserForm, id)
                    .addObject("duplicatedUsername", ex.isDuplicatedUsername());
        }
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/500");
        }
        System.out.println(opUser.get().getUsername()+"\n"+opUser.get().getId()+"\n");
        return new ModelAndView("redirect:/user/" + opUser.get().getId());
    }

    @RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.POST }, params={"update-password"})
    public ModelAndView editPassword(@Validated({ChangePasswordEditUser.class}) @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                 final BindingResult errors, HttpServletRequest request,
                                 @PathVariable("id") long id) {

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n PASSWORD \n\n\n\n\n\n\n\n\n\n\n\n");

        if (errors.hasErrors()) {
            return editUserForm(editUserForm, id);
        }
        if(loggedUser().getId() != id) {
            return new ModelAndView("redirect:/403");
        }
        Optional<User> opUser = userService.updatePassword(getLocale(), editUserForm.getNewPassword(), id);
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/500");
        }
        return new ModelAndView("redirect:/user/" + opUser.get().getId());

    }
    
    
    @RequestMapping(value = "/test")
    public ModelAndView testUsers() {
        final ModelAndView mav = new ModelAndView("views/test");
        userService.removeUser(26);
        mav.addObject("string",
                userService.findById(getLocale(),26).get().getStatus().getName());
        return mav;
    }
}