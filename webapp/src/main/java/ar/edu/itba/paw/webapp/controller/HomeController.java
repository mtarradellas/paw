package ar.edu.itba.paw.webapp.controller;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.PasswordDto;
import ar.edu.itba.paw.webapp.dto.SpeciesDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;

@Component
@Path("/")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private SpeciesService speciesService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAvailable() {

        final String locale = ApiUtils.getLocale();
        final List<SpeciesDto> speciesList = speciesService.speciesList(locale).stream().map(s -> SpeciesDto.fromSpecies(s, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<SpeciesDto>>(speciesList) {}).build();
    }

    @POST
    @Path("/register")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createUser(final UserDto user) {
        try {
            ParseUtils.parseUser(user);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        final String locale = ApiUtils.getLocale();
        Optional<User> opNewUser;
        try {
            opNewUser = userService.create(user.getUsername(), user.getPassword(), user.getMail(), locale, uriInfo.getBaseUri().toString());
        } catch (DataIntegrityViolationException | UserException ex) {
            LOGGER.warn("User creation failed with exception");
            String msg = ex.getMessage();
            LOGGER.warn("{}", msg);
            final int code = msg.contains("username")? 2 : 3;
            msg = msg.contains("username")? "Username " : "Mail "; 
            final ErrorDto body = new ErrorDto(code, msg + "already exists.");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if (!opNewUser.isPresent()) {
            LOGGER.warn("User creation failed, service returned empty user");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final URI userUri = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(opNewUser.get().getId())).build();
        return Response.created(userUri).build();
    }

    @POST
    @Path("/activate-account")
    public Response activateAccount(@QueryParam("token") String token) {
        
        if (token == null) {
            LOGGER.warn("Token parameter null.");
            return Response.status(Status.BAD_REQUEST.getStatusCode()).build();
        }
        final UUID uuid = UUID.fromString(token);

        try {
            userService.activateAccountWithToken(uuid);
        } catch (NotFoundException | UserException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/request-password-reset")
    public Response requestPasswordReset(final UserDto dto) {
        if (dto == null || dto.getMail() == null) {
            LOGGER.warn("Mail parameter null.");
            return Response.status(Status.BAD_REQUEST.getStatusCode()).build();
        }

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
            userService.requestPasswordReset(dto.getMail(), baseUrl);
        } catch (NotFoundException ex) {
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return Response.noContent().build();
    }
    
    @POST
    @Path("/password-reset")
    public Response passwordReset(final PasswordDto dto) {
        if (dto == null || dto.getNewPassword() == null || dto.getToken() == null) {
            return Response.status(Status.BAD_REQUEST.getStatusCode()).build();
        }
        
        UUID uuid = UUID.fromString(dto.getToken());
        String password = dto.getNewPassword();
        
        try {
            userService.resetPassword(uuid, password);
        } catch (NotFoundException | UserException ex) {
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return Response.noContent().build();
    }

//    public ModelAndView getAdminHome() {
//        return new ModelAndView("admin/admin");
//    }

}
