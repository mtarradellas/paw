package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.InvalidPasswordException;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.UserStatus;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.PasswordDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;

@Component
@Path("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/logged-user")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getLoggedUser(@Context HttpServletRequest request) {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = ApiUtils.loggedUser(request, userService, auth);

        final boolean isAdmin = userService.isAdmin(user);
        UserDto userDto = UserDto.fromUser(user, uriInfo);
        userDto.setIsAdmin(isAdmin);
        userDto.setMail(user.getMail());

        return Response.ok(new GenericEntity<UserDto>(userDto){}).build();
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

    @GET
    @Path("/{userId}/mail")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserMail(@Context HttpServletRequest request, @PathParam("userId") long userId) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = ApiUtils.loggedUser(request, userService, auth);

        String mail;
        try {
            mail = userService.getMail(user, userId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (UserException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        UserDto userMail = new UserDto();
        userMail.setMail(mail);
        return Response.ok(new GenericEntity<UserDto>(userMail){}).build();        
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@Context HttpServletRequest request, @PathParam("userId") long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ApiUtils.loggedUser(request, userService, auth);
        if (user == null || user.getId() != userId) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        try{
            userService.removeUser(userId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        LOGGER.debug("User {} removed", userId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/edit/username")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateUsername(@Context HttpServletRequest request,
                                   @PathParam("userId") long userId,
                                   final UserDto user) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(request, userService, auth);
        if (currentUser == null || currentUser.getId() != userId) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        if (user == null) return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        try {
            ParseUtils.parseUsername(user.getUsername());
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage()); 
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<User> opUser;
        try {
            opUser = userService.updateUsername(userId, user.getUsername());
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(3, "Username already taken.");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }

        if(!opUser.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/edit/password")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updatePassword(@Context HttpServletRequest request,
                                   @PathParam("userId") long userId,
                                   final PasswordDto dto) {
        
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(request, userService, auth);
        if (currentUser == null || currentUser.getId() != userId) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(1, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        try {
            ParseUtils.parsePassword(dto.getOldPassword());
            ParseUtils.parsePassword(dto.getNewPassword());
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<User> opUser;
        try {
            opUser = userService.updatePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        } catch(InvalidPasswordException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }

        if(!opUser.isPresent()){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }
}