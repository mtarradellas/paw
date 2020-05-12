package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Controller
public class UserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page) {

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();

        if (page == null){
            page = "1";
        }
        List<Pet> petsByUser = petService.getByUserId(locale, id, page).collect(Collectors.toList());
        List<Pet> petsAvailableByUser = new ArrayList<>();
        for (Pet pet : petsByUser) {
            if(pet.getStatus().getId() == PetStatus.AVAILABLE.getValue()) {
                petsAvailableByUser.add(pet);
            }
        }
        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petService.getMaxUserPetsPages(id));
        Optional<User> opUser = userService.findById(locale, id);
        if (!opUser.isPresent()) throw new UserNotFoundException("User " + id + " not found");
        mav.addObject("user", opUser.get());
        mav.addObject("userPets", petsByUser.toArray());
        mav.addObject("userAvailablePets", petsAvailableByUser);
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
            List<Request> requestList = requestService.filterListByOwner(locale, user.getId(), status, searchCriteria, searchOrder).collect(Collectors.toList());
            mav.addObject("requests_list", requestList);
            mav.addObject("list_size", requestList.size());
        }
        /* Default request list */
        else {
            List<Request> requestList = requestService.listByOwner(locale, user.getId()).collect(Collectors.toList());
            mav.addObject("requests_list",requestList);
            mav.addObject("list_size", requestList.size());
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

    @RequestMapping(value = "/requests/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView recoverRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.recover(id, user.getId(), locale)) {
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
            List<Request> requestList = requestService.filterListByPetOwner(locale, user.getId(), status, searchCriteria, searchOrder).collect(Collectors.toList());
            mav.addObject("interests_list", requestList);
            mav.addObject("list_size", requestList.size());
        }
        /* Default interest list */
        else{
            List<Request> requestList = requestService.listByPetOwner(locale, user.getId()).collect(Collectors.toList());
            mav.addObject("interests_list", requestList);
            mav.addObject("list_size", requestList.size());
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

        return editUserForm(populateForm(editUserForm, id), id);
    }

    private EditUserForm populateForm(EditUserForm editUserForm, long id){

        final String locale = getLocale();

        User user = userService.findById(locale, id).orElseThrow(UserNotFoundException::new);

        editUserForm.setPhone(user.getPhone());
        editUserForm.setUsername(user.getUsername());

        return editUserForm;
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
        authenticateUserAndSetSession(opUser.get().getUsername(),request);
        return new ModelAndView("redirect:/user/" + opUser.get().getId());
    }

    @RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.POST }, params={"update-password"})
    public ModelAndView editPassword(@Validated({ChangePasswordEditUser.class}) @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                 final BindingResult errors, HttpServletRequest request,
                                 @PathVariable("id") long id) {

        if (errors.hasErrors()) {
            populateForm(editUserForm, id);
            return editUserForm(editUserForm, id);
        }
        if(loggedUser().getId() != id) {
            return new ModelAndView("redirect:/403");
        }
        Optional<User> opUser;
        try {
            opUser = userService.updatePassword(getLocale(), editUserForm.getCurrentPassword(), editUserForm.getNewPassword(), id);
        }
        catch(InvalidPasswordException ex) {
            return editUserForm(editUserForm, id).addObject("current_password_fail", true);
        }
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

    //TODO
    @RequestMapping(value = "/user/{id}/remove", method = {RequestMethod.POST})
    public ModelAndView userUpdateRemoved(@PathVariable("id") long id) {
        User user = loggedUser();
        if (user != null && id == user.getId()) {
            userService.removeUser(id);
            LOGGER.debug("User {} updated as removed", id);
            return new ModelAndView("redirect:/logout");
        }
        LOGGER.warn("User is not logged user, status not updated");
        return new ModelAndView("redirect:/403");
    }
}