package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exception.InvalidPasswordException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditUserForm;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController extends ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RequestService requestService;

    private static final int REQ_PAGE_SIZE = 25;
    private static final int PET_PAGE_SIZE = 4;
    private static final int REV_PAGE_SIZE = 20;

    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 5;

    @RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page,
                             @RequestParam(name = "descriptionTooLong", required = false) String toolong,
                             @RequestParam(name = "showAllReviews", required = false) String showAllReviews) {

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();

        int pageNum = parsePage(page);

        List<Pet> petList = petService.listByUser(locale, id, pageNum, PET_PAGE_SIZE);
        int amount = petService.getListByUserAmount(locale, id);
        Optional<User> opUser = userService.findById(id);
        User user = opUser.orElseThrow(UserNotFoundException::new);

        boolean canRate = false;

        if(loggedUser() != null && !(user.getId() == loggedUser().getId())){
            for(Request request : loggedUser().getRequestList()){
                if((request.getPet().getUser().getId() == user.getId()) && (request.getStatus().getValue() ==
                        RequestStatus.ACCEPTED.getValue())){
                    canRate = true;
                }
            }
            for(Review review : user.getTargetReviews()){
                if(review.getOwner().getId() == loggedUser().getId()){
                    canRate = false;
                }
            }
        }

        if(toolong != null && toolong.equals("true")){
            mav.addObject("descriptionTooLong", true);
        }else{
            mav.addObject("descriptionTooLong", false);
        }

        if(showAllReviews == null || (!showAllReviews.equals("true") && !showAllReviews.equals("false"))){
            showAllReviews = "false";
        }

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
        mav.addObject("userPets", petList);
        mav.addObject("amount", amount);
        mav.addObject("user", user);
        mav.addObject("canRate", canRate);
        mav.addObject("showAllReviews", showAllReviews);
        return mav;
    }


    @RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                    @RequestParam(name = "page", required = false) String page,
                                    @RequestParam(name = "find", required = false) String find) {

        final ModelAndView mav = new ModelAndView("views/requests");
        final User user = loggedUser();

        int pageNum = parsePage(page);
        RequestStatus requestStatus = parseStatus(RequestStatus.class, status);
        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);

        if (!isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = parseFind(find);

        List<Request> requestList = requestService.filteredList(user, null, findList, requestStatus,
                    searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
        int amount = requestService.getFilteredListAmount(user, null, findList, requestStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REQ_PAGE_SIZE));
        mav.addObject("requestList", requestList);
        mav.addObject("amount", amount);
        return mav;
    }

    @RequestMapping(value = "/requests/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.cancel(id, user, baseUrl)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/requests/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView recoverRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.recover(id, user, baseUrl)) {
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

        searchCriteria = parseCriteria(searchCriteria);
        searchOrder = parseOrder(searchOrder);

        int pageNum = parsePage(page);
        RequestStatus requestStatus = parseStatus(RequestStatus.class, status);

        if (!isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = parseFind(find);

        List<Request> requestList = requestService.filteredListByPetOwner(user, null, findList, requestStatus,
                searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
        int amount = requestService.getFilteredListByPetOwnerAmount(user, null, findList, requestStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REQ_PAGE_SIZE));
        mav.addObject("interestList", requestList);
        mav.addObject("amount", amount);

        return mav;
    }

    @RequestMapping(value = "/interests/{id}/accept", method = {RequestMethod.POST})
    public ModelAndView acceptInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.accept(id, user, baseUrl)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    @RequestMapping(value = "/interests/{id}/reject", method = {RequestMethod.POST})
    public ModelAndView rejectInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.reject(id, user, baseUrl)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }


    @RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.GET })
    public ModelAndView editUserGet(@ModelAttribute("editUserForm") final EditUserForm editUserForm, @PathVariable("id") long id){

        if(loggedUser().getId() != id) {
            return new ModelAndView("redirect:/403" );
        }

        return editUserForm(populateForm(editUserForm, id), id);
    }

    private EditUserForm populateForm(EditUserForm editUserForm, long id){

        User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        editUserForm.setUsername(user.getUsername());

        return editUserForm;
    }

    private ModelAndView editUserForm(@ModelAttribute("editUserForm") final EditUserForm editUserForm, long id) {

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
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return editUserForm(editUserForm, id)
                    .addObject("duplicatedUsername", ex.getMessage().contains("users_username_key"));
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
            return editUserForm(editUserForm, id).addObject("currentPasswordFail", true);
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

    @RequestMapping(value = "/user/{id}/review", method = {RequestMethod.POST})
    public ModelAndView uploadReview(@PathVariable("id") long id,
                                     @RequestParam(name = "score") String scoreStr,
                                     @RequestParam(name = "description") String description) {

        if(description.length() > 200){
            return new ModelAndView("redirect:/user/" + id).addObject("descriptionTooLong",
                    true);

        }
        System.out.println("ENTRE\n\n\n\n\n");
        User user = loggedUser();
        if (user != null) {
            int score = parseReviewScore(scoreStr);
            try {
                System.out.println("\n\n\n\n\n\nRIGBY" + user + " " + id + " "+ score + " " + description);
                userService.addReview(user, id, score, description);
            } catch (DataIntegrityViolationException ex) {
                System.out.println("\n\n\n\n\n\n\n\nMENEM ");
                LOGGER.warn("{}", ex.getMessage());
                return new ModelAndView("redirect:/user/" + id);
            }
            return new ModelAndView("redirect:/user/" + id);
        }
        return new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/user/{id}/reviews")
    public ModelAndView reviewList(@PathVariable("id") long id,
                                   @RequestParam(name = "owner", required = false) String owner,
                                   @RequestParam(name = "minscore", required = false) String minscore,
                                   @RequestParam(name = "maxscore", required = false) String maxscore,
                                   @RequestParam(name = "status", required = false) String status,
                                   @RequestParam(name = "criteria", required = false) String criteria,
                                   @RequestParam(name = "order", required = false) String order,
                                   @RequestParam(name = "page", required = false) String page) {

        Long ownerId = parseUser(owner);
        int min = parseScore(minscore, MIN_SCORE);
        int max = parseScore(maxscore, MAX_SCORE);
        ReviewStatus reviewStatus = parseStatus(ReviewStatus.class, status);
        criteria = parseCriteria(criteria);
        order = parseOrder(order);
        int pageNum = parsePage(page);

        List<Review> reviewList = userService.reviewList(ownerId, id, min, max, reviewStatus, criteria, order,
                pageNum, REV_PAGE_SIZE);
        int amount = userService.getReviewListAmount(ownerId, id, min, max, reviewStatus);

        Optional<User> opUser = userService.findById(id);
        User user = opUser.orElseThrow(UserNotFoundException::new);

        ModelAndView mav = new ModelAndView("views/reviews");
        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REV_PAGE_SIZE));
        mav.addObject("reviewList", reviewList);
        mav.addObject("amount", amount);
        mav.addObject("user", user);
        return mav;
    }

    private Authentication authenticateUserAndSetSession(String username, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return authentication;
    }
}