package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exception.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.PetList;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.PetStatus;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
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

    private static final int REQ_PAGE_SIZE = 25;

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page) {

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();

        if (page == null){
            page = "1";
        }
        PetList petsByUser = petService.getByUserId(locale, id, page);

        mav.addObject("currentPage", page);
        mav.addObject("maxPage", petsByUser.getMaxPage());
        Optional<User> opUser = userService.findById(id);
        if (!opUser.isPresent()) throw new UserNotFoundException("User " + id + " not found");
        mav.addObject("user", opUser.get());
        mav.addObject("userPets", petsByUser);
        mav.addObject("totalPets", petsByUser.getTotalPetsAmount());
        return mav;
    }

    @RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                    @RequestParam(name = "page", required = false) String page,
                                    @RequestParam(name = "find", required = false) String find) {

        final ModelAndView mav = new ModelAndView("views/requests");
        final String locale = getLocale();
        final User user = loggedUser();

        int pageNum = 1;
        if(page != null) {
            try {
                pageNum = Integer.parseInt(page);
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Invalid page parameter");
            }
        }

        RequestStatus requestStatus = null;
        if(status != null) {
            try {
                int idx = Integer.parseInt(status);
                requestStatus = RequestStatus.values()[idx];
                if (requestStatus == null) throw new BadRequestException("Invalid status parameter");
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Invalid status parameter");
            }
        }

        if(find != null && !find.matches("^[a-zA-Z0-9 \u00C0-\u00D6\u00D8-\u00f6\u00f8-\u00ff-]*$")) {
            mav.addObject("wrongSearch", true);
            find = "";
        } else {
            mav.addObject("wrongSearch", false);
        }

        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered request list */
        if (status != null || searchCriteria != null) {
            List<Request> requestList = requestService.filteredList(user, null, find, requestStatus,
                    searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
            int amount = requestService.getFilteredListAmount(user, null, find, requestStatus);
            mav.addObject("requestList", requestList);
            mav.addObject("amount", amount);
        }
        return mav;
    }

    @RequestMapping(value = "/requests/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.cancel(id, user)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/requests/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView recoverRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.recover(id, user)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                      @RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "find", required = false) String find) {

        final ModelAndView mav = new ModelAndView("views/interests");
        final User user = loggedUser();
        final String locale = getLocale();

        int pageNum = 1;
        if(page != null) {
            try {
                pageNum = Integer.parseInt(page);
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Invalid page parameter");
            }
        }

        RequestStatus requestStatus = null;
        if(status != null) {
            try {
                int idx = Integer.parseInt(status);
                requestStatus = RequestStatus.values()[idx];
                if (requestStatus == null) throw new BadRequestException("Invalid status parameter");
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Invalid status parameter");
            }
        }

        if(find != null && !find.matches("^[a-zA-Z0-9 \u00C0-\u00D6\u00D8-\u00f6\u00f8-\u00ff-]*$")) {
            mav.addObject("wrongSearch", true);
            find = "";
        } else {
            mav.addObject("wrongSearch", false);
        }

        searchCriteria = (searchCriteria == null || searchCriteria.equals("any") ? null : searchCriteria);

        /* Filtered interest list */
        if(status != null || searchCriteria != null) {
            List<Request> requestList = requestService.filteredListByPetOwner(user, null, find, requestStatus,
                    searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
            int amount = requestService.getFilteredListByPetOwnerAmount(user, null, find, requestStatus);
            mav.addObject("interestList", requestList);
            mav.addObject("amount", amount);
        }
        return mav;
    }

    @RequestMapping(value = "/interests/{id}/accept", method = {RequestMethod.POST})
    public ModelAndView acceptInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.accept(id, user)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests/{id}/reject", method = {RequestMethod.POST})
    public ModelAndView rejectInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String locale = getLocale();

        if (requestService.reject(id, user)) {
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

        User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        editUserForm.setUsername(user.getUsername());

        return editUserForm;
    }

    private ModelAndView editUserForm(@ModelAttribute("editUserForm") final EditUserForm editUserForm, long id) {
        final String locale = getLocale();

        return new ModelAndView("views/user_edit")
                .addObject("user",
                        userService.findById(id).orElseThrow(UserNotFoundException::new))
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
             opUser = userService.updateUsername(id, editUserForm.getUsername());
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
            opUser = userService.updatePassword(id, editUserForm.getCurrentPassword(), editUserForm.getNewPassword());
        }
        catch(InvalidPasswordException ex) {
            return editUserForm(editUserForm, id).addObject("current_password_fail", true);
        }
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/500");
        }
        return new ModelAndView("redirect:/user/" + opUser.get().getId());

    }


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