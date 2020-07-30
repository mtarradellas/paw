package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int USR_PAGE_SIZE = 25;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    /* TODO this method should not be here?. Users are listed only on admin page */
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserList(@QueryParam("page") @DefaultValue("1") int page) {

        try {
            ParseUtils.parsePage(page);
        } catch (BadRequestException ex) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        final List<UserDto> userList = userService.list(page, USR_PAGE_SIZE).stream()
                .map(u -> UserDto.fromUserForList(u, uriInfo)).collect(Collectors.toList());
        final int amount = userService.getListAmount();

        return ApiUtils.paginatedListResponse(amount, USR_PAGE_SIZE, page, uriInfo, new GenericEntity<List<UserDto>>(userList) {});
    }

    @GET
    @Path("/{userId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("userId") long userId) {
        final Optional<User> opUser = userService.findById(userId);

        if (!opUser.isPresent()) {
            LOGGER.debug("User {} not found", userId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        final User user = opUser.get();
        return Response.ok(new GenericEntity<UserDto>(UserDto.fromUser(user, uriInfo)){}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createUser(final UserDto user) {
        final String locale = ApiUtils.getLocale();

        Optional<User> opNewUser;
        try {
            opNewUser = userService.create(user.getUsername(), user.getPassword(), user.getMail(), locale, uriInfo.getPath());
        } catch (DataIntegrityViolationException | UserException ex) {
            LOGGER.warn("User creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if (!opNewUser.isPresent()) {
            LOGGER.warn("User creation failed, service returned empty user");
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

//    @GET
//    @Path("/{userId}/reviews")
//    public Response getUserReviews(@PathParam("userId") long userId) {
//
//        final Optional<User> opUser = userService.findById(userId);
//
//        if (!opUser.isPresent()) {
//            LOGGER.debug("User {} not found", userId);
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//        }
//
//        final List<ReviewDto> reviewList = opUser.get().getTargetReviews()
//                .stream().map(RequestDto::fromRequest).collect(Collectors.toList());
//        return Response.ok(new GenericEntity<List<RequestDto>>(interestList) {
//        }).build();
//
//    }

//    @GET
//    @Path("/{userId}/reviews/info")


    /*
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * OLD CONTROLLER METHODS * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * */
/*
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

 */
}