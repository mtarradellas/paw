package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PetService;
import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.InvalidPasswordException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.webapp.dto.RequestDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditUserForm;
import ar.edu.itba.paw.webapp.form.groups.BasicInfoEditUser;
import ar.edu.itba.paw.webapp.form.groups.ChangePasswordEditUser;
import ar.edu.itba.paw.webapp.util.ParseUtils;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/users")
public class UserController extends BaseController {

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
    private static final int PET_PAGE_SIZE = 8;
    private static final int REV_PAGE_SIZE = 5;

    /* TODO remove. Only here for testing */
    private static final int USR_PAGE_SIZE = 25;

    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 5;

    @Context
    private UriInfo uriInfo;

    /* TODO this method should not be here. Users are listed only on admin page */
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserList(@QueryParam("page") @DefaultValue("1") int page) {

        final List<UserDto> userList = userService.list(page, USR_PAGE_SIZE).stream()
                .map(u -> UserDto.fromUserForList(u, uriInfo)).collect(Collectors.toList());
        final int amount = userService.getListAmount();

        final int firstPage = 1;
        final int lastPage  = (int) Math.ceil((double) amount / USR_PAGE_SIZE);
        final int prevPage  = (page == 1) ? lastPage : page - 1;
        final int nextPage  = (page == lastPage) ? firstPage : page + 1;

        final URI first = uriInfo.getAbsolutePathBuilder().queryParam("page", firstPage).build();
        final URI last  = uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build();
        final URI prev  = uriInfo.getAbsolutePathBuilder().queryParam("page", prevPage).build();
        final URI next  = uriInfo.getAbsolutePathBuilder().queryParam("page", nextPage).build();

        return Response.ok(new GenericEntity<List<UserDto>>(userList) {})
                .link(first, "first")
                .link(last, "last")
                .link(prev, "prev")
                .link(next, "next")
                .build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response create(final UserDto user) {
        final String locale = getLocale();

        final Optional<User> opNewUser = userService.create(user.getUsername(), user.getPassword(), user.getMail(), locale, uriInfo.getPath());
        if (!opNewUser.isPresent()) {
            LOGGER.warn("User creation failed");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(opNewUser.get().getId())).build();
        return Response.created(userUri).build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) {
        userService.removeUser(userId);
        LOGGER.debug("User {} removed", userId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") long userId) {
        final Optional<User> opUser = userService.findById(userId);

        if (!opUser.isPresent()) {
            LOGGER.debug("User {} not found", userId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        final User user = opUser.get();
        return Response.ok(new GenericEntity<UserDto>(UserDto.fromUser(user, uriInfo)){}).build();
    }

    @GET
    @Path("/{userId}/requests")
    public Response getUserRequests(@PathParam("userId") long userId) {

        final Optional<User> opUser = userService.findById(userId);

        if (!opUser.isPresent()) {
            LOGGER.debug("User {} not found", userId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }

        final List<RequestDto> requestList = opUser.get().getRequestList()
                .stream().map(RequestDto::fromRequest).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RequestDto>> (requestList) {}).build();
    }

    @GET
    @Path("/{userId}/interests")
    public Response getUserInterests(@PathParam("userId") long userId) {

        final Optional<User> opUser = userService.findById(userId);

        if (!opUser.isPresent()) {
            LOGGER.debug("User {} not found", userId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }

        final List<RequestDto> interestList = opUser.get().getInterestList()
                .stream().map(RequestDto::fromRequest).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RequestDto>> (interestList) {}).build();
    }

    /*
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * OLD CONTROLLER METHODS * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * */

    //@RequestMapping(value = "/user/{id}")
    public ModelAndView user(@PathVariable("id") long id,
                             @RequestParam(name = "page", required = false) String page,
                             @RequestParam(name = "descriptionTooLong", required = false) String toolong,
                             @RequestParam(name = "showAllReviews", required = false) String showAllReviews,
                             @RequestParam(name = "showAllAdopted", required = false) String showAllAdopted) {

        final ModelAndView mav = new ModelAndView("views/single_user");
        final String locale = getLocale();
        final User loggedUser = loggedUser();

        int pageNum = ParseUtils.parsePage(page);

        List<Pet> petList = petService.listByUser(locale, id, pageNum, PET_PAGE_SIZE);
        int amount = petService.getListByUserAmount(locale, id);
        Optional<User> opUser = userService.findById(id);
        User user = opUser.orElseThrow(UserNotFoundException::new);
        double reviewAverage = userService.getReviewAverage(id);
        int reviewAmount = userService.getReviewListAmount(null, id, 0, -1, ReviewStatus.VALID);
        boolean acceptedRequest = user.getInterestList().stream()
                .filter(r -> r.getStatus().equals(RequestStatus.ACCEPTED) || r.getStatus().equals(RequestStatus.SOLD))
                .anyMatch(r -> r.getUser().getId().equals(loggedUser.getId()));

        boolean canRate = false;

        if (loggedUser != null && !user.getId().equals(loggedUser.getId())) {
            boolean acquiredPet = loggedUser.getNewPets().stream().anyMatch(p -> p.getUser().getId().equals(user.getId()));
            if (acquiredPet) {
                canRate = loggedUser.getOwnerReviews().stream().noneMatch(r -> r.getTarget().getId().equals(user.getId()));
            }
        }

        mav.addObject("descriptionTooLong", toolong != null && toolong.equals("true"));

        if(showAllReviews == null || (!showAllReviews.equals("true") && !showAllReviews.equals("false"))){
            showAllReviews = "false";
        }

        if(showAllAdopted == null || (!showAllAdopted.equals("true") && !showAllAdopted.equals("false"))){
            showAllAdopted = "false";
        }

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / PET_PAGE_SIZE));
        mav.addObject("userPets", petList);
        mav.addObject("amount", amount);
        mav.addObject("user", user);
        mav.addObject("canRate", canRate);
        mav.addObject("showAllReviews", showAllReviews);
        mav.addObject("showAllAdopted", showAllAdopted);
        mav.addObject("reviewAverage", reviewAverage);
        mav.addObject("reviewAmount", reviewAmount);
        mav.addObject("acceptedRequest", acceptedRequest);
        return mav;
    }

    //@RequestMapping(value = "/user/{id}/reviews")
    public @ResponseBody
    Map<String, Object> userReviews(@PathVariable("id") long id,
                                    @RequestParam(name = "owner", required = false) String owner,
                                    @RequestParam(name = "minscore", required = false) String minscore,
                                    @RequestParam(name = "maxscore", required = false) String maxscore,
                                    @RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "criteria", required = false) String criteria,
                                    @RequestParam(name = "order", required = false) String order,
                                    @RequestParam(name = "page", required = false) String page) {

        Long ownerId = ParseUtils.parseUser(owner);
        int min = ParseUtils.parseScore(minscore, MIN_SCORE);
        int max = ParseUtils.parseScore(maxscore, MAX_SCORE);
        ReviewStatus reviewStatus = ParseUtils.parseStatus(ReviewStatus.class, status);
        criteria = ParseUtils.parseCriteria(criteria);
        order = ParseUtils.parseOrder(order);
        int pageNum = ParseUtils.parsePage(page);
        List<Review> reviewList = userService.reviewList(ownerId, id, min, max, reviewStatus, criteria, order,
                pageNum, REV_PAGE_SIZE);
        int amount = userService.getReviewListAmount(ownerId, id, min, max, reviewStatus);

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> reviews = reviewList.stream().map(review -> {
            Map<String, Object> comm = new HashMap<>();
            comm.put("review", review.toJson());
            return comm;
        }).collect(Collectors.toList());

        response.put("currentPage", pageNum);
        response.put("maxPage", (int) Math.ceil((double) amount / REV_PAGE_SIZE));
        response.put("reviewList", reviews);
        response.put("amount", amount);

        return response;
    }

    //@RequestMapping(value = "/requests")
    public ModelAndView getRequests(@RequestParam(name = "status", required = false) String status,
                                    @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                    @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                    @RequestParam(name = "page", required = false) String page,
                                    @RequestParam(name = "find", required = false) String find) {

        final ModelAndView mav = new ModelAndView("views/requests");
        final User user = loggedUser();

        int pageNum = ParseUtils.parsePage(page);
        RequestStatus requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
        searchCriteria = ParseUtils.parseCriteria(searchCriteria);
        searchOrder = ParseUtils.parseOrder(searchOrder);

        if (!ParseUtils.isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = ParseUtils.parseFind(find);

        requestService.logRequestsAccess(user);

        List<Request> requestList = requestService.filteredList(user, null, findList, requestStatus,
                    searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
        List<RequestStatus> statusList;
        if(requestStatus == null) statusList = requestService.filteredStatusList(user, null, findList, null);
        else {
            statusList = new ArrayList<>();
            statusList.add(requestStatus);
        }
        int amount = requestService.getFilteredListAmount(user, null, findList, requestStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REQ_PAGE_SIZE));
        mav.addObject("requestList", requestList);
        mav.addObject("amount", amount);
        mav.addObject("statusList", statusList);
        return mav;
    }

    //@RequestMapping(value = "/requests/{id}/cancel", method = {RequestMethod.POST})
    public ModelAndView cancelRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.cancel(id, user, baseUrl)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    //@RequestMapping(value = "/requests/{id}/recover", method = {RequestMethod.POST})
    public ModelAndView recoverRequest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.recover(id, user, baseUrl)) {
            return new ModelAndView("redirect:/requests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    //@RequestMapping(value = "/interests")
    public ModelAndView getInterested(@RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
                                      @RequestParam(name = "searchOrder", required = false) String searchOrder,
                                      @RequestParam(name = "page", required = false) String page,
                                      @RequestParam(name = "find", required = false) String find,
                                      @RequestParam(name = "petId", required = false) String petId) {

        final ModelAndView mav = new ModelAndView("views/interests");
        final User user = loggedUser();



        searchCriteria = ParseUtils.parseCriteria(searchCriteria);
        searchOrder = ParseUtils.parseOrder(searchOrder);
        Long pet = ParseUtils.parsePet(petId);

        int pageNum = ParseUtils.parsePage(page);
        RequestStatus requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);

        if (!ParseUtils.isAllowedFind(find)) {
            mav.addObject("wrongSearch", true);
            find = null;
        } else {
            mav.addObject("wrongSearch", false);
        }
        List<String> findList = ParseUtils.parseFind(find);

        requestService.logInterestsAccess(user);

        List<Request> requestList = requestService.filteredListByPetOwner(user, pet, findList, requestStatus,
                searchCriteria, searchOrder, pageNum, REQ_PAGE_SIZE);
        List<Pet> availablePets = requestService.filteredPetListByPetOwner(user, pet, findList, requestStatus);

        List<RequestStatus> statusList;
        if(requestStatus == null) {
            statusList = requestService.filteredStatusListByPetOwner(user, null, findList, null);
        }
        else {
            statusList = new ArrayList<>();
            statusList.add(requestStatus);
        }
        int amount = requestService.getFilteredListByPetOwnerAmount(user, pet, findList, requestStatus);

        mav.addObject("currentPage", pageNum);
        mav.addObject("maxPage", (int) Math.ceil((double) amount / REQ_PAGE_SIZE));
        mav.addObject("interestList", requestList);
        mav.addObject("amount", amount);
        mav.addObject("availablePets", availablePets);
        mav.addObject("statusList", statusList);

        return mav;
    }

    //@RequestMapping(value = "/interests/{id}/accept", method = {RequestMethod.POST})
    public ModelAndView acceptInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.accept(id, user, baseUrl)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }

    //@RequestMapping(value = "/interests/{id}/reject", method = {RequestMethod.POST})
    public ModelAndView rejectInterest(@PathVariable("id") long id) {
        final User user = loggedUser();
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        if (requestService.reject(id, user, baseUrl)) {
            return new ModelAndView("redirect:/interests" );
        }
        return new ModelAndView("redirect:/403" );
    }


    //@RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.GET })
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

    //@RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.POST }, params={"update-basic-info"})
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
                    .addObject("loggedUser", loggedUser())
                    .addObject("duplicatedUsername", ex.getMessage().contains("users_username_key"));
        }
        if(!opUser.isPresent()){
            return new ModelAndView("redirect:/500");
        }
        authenticateUserAndSetSession(opUser.get().getUsername(),request);
        return new ModelAndView("redirect:/user/" + opUser.get().getId());
    }

    //@RequestMapping(value = "/edit-user/{id}", method = { RequestMethod.POST }, params={"update-password"})
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


    //@RequestMapping(value = "/user/{id}/remove", method = {RequestMethod.POST})
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

    //@RequestMapping(value = "/user/{id}/review", method = {RequestMethod.POST})
    public ModelAndView uploadReview(@PathVariable("id") long id,
                                     @RequestParam(name = "score") String scoreStr,
                                     @RequestParam(name = "description") String description) {

        if(description.length() > 200){
            return new ModelAndView("redirect:/user/" + id).addObject("descriptionTooLong",
                    true);

        }
        User user = loggedUser();
        if (user != null) {
            int score = ParseUtils.parseReviewScore(scoreStr);
            try {
                userService.addReview(user, id, score, description);
            } catch (DataIntegrityViolationException ex) {
                LOGGER.warn("{}", ex.getMessage());
                return new ModelAndView("redirect:/user/" + id);
            }
            return new ModelAndView("redirect:/user/" + id);
        }
        return new ModelAndView("redirect:/403");
    }

    private Authentication authenticateUserAndSetSession(String username, HttpServletRequest request){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return authentication;
    }
}