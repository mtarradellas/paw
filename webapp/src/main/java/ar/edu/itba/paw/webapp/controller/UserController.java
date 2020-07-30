package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.InvalidPasswordException;
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
import org.springframework.web.servlet.ModelAndView;

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
            opNewUser = userService.create(user.getUsername(), user.getPassword(), user.getMail(), locale, uriInfo.getBaseUri().toString());
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

    @POST
    @Path("/{userId}/edit/username")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateUsername(@PathParam("userId") long userId,
                                   @QueryParam("username") String username) {
        Optional<User> opUser;
        try {
            opUser = userService.updateUsername(userId, username);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(!opUser.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/edit/password")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updatePassword(@PathParam("userId") long userId,
                                   @QueryParam("oldPassword") String oldPassword,
                                   @QueryParam("newPassword") String newPassword) {
        Optional<User> opUser;
        try {
            opUser = userService.updatePassword(userId, oldPassword, newPassword);
        }
        catch(InvalidPasswordException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        if(!opUser.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }
}