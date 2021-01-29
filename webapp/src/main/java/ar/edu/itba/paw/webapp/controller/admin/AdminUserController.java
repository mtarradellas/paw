package ar.edu.itba.paw.webapp.controller.admin;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

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

@Controller
@Path("/admin/users")
public class AdminUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);

    private static final int USR_PAGE_SIZE = 24;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserList(@Context HttpServletRequest httpRequest,
                                @QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("status") @DefaultValue("-1") int status,
                                @QueryParam("find") String find,
                                @QueryParam("searchCriteria") String searchCriteria,
                                @QueryParam("searchOrder") String searchOrder) {

        UserStatus userStatus;
        try {
            ParseUtils.parsePage(page);
            userStatus = ParseUtils.parseStatus(UserStatus.class, status);
            ParseUtils.isAllowedFind(find);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
        } catch (BadRequestException ex) {
            final ErrorDto body = new ErrorDto(1, ex.getMessage()); 
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        List<String> findList = ParseUtils.parseFind(find);

        final List<UserDto> userList = userService.filteredList(findList, userStatus, searchCriteria, searchOrder, page, USR_PAGE_SIZE)
                .stream().map(u -> UserDto.fromUserForList(u, uriInfo)).collect(Collectors.toList());
        final int amount = userService.getFilteredAmount(findList, userStatus);

        String query = httpRequest.getQueryString();
        query = query == null? null : query.replaceAll("&?page=.*&?", "");
        return ApiUtils.paginatedListResponse(amount, USR_PAGE_SIZE, page, uriInfo, userList, query, null);
    }

    @GET
    @Path("/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserListFilters(@QueryParam("status") @DefaultValue("-1") int status,
                                       @QueryParam("find") String find) {

        UserStatus userStatus;
        try {
            userStatus = ParseUtils.parseStatus(UserStatus.class, status);
            ParseUtils.isAllowedFind(find);
        } catch (BadRequestException ex) {
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        List<String> findList = ParseUtils.parseFind(find);

        List<UserStatus> statusList;
        if(userStatus != null) {
            statusList = new ArrayList<>();
            statusList.add(userStatus);
        }
        else {
            statusList = userService.filteredStatusList(findList, null);
        }
        List<Integer> statusIdList = statusList.stream().map(UserStatus::getValue).collect(Collectors.toList());

        Map<String, Object> filters = new TreeMap<>();
        filters.put("statusList", statusIdList);

        return Response.ok().entity(new Gson().toJson(filters)).build();
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
        return Response.ok(new GenericEntity<UserDto>(UserDto.fromUserAdmin(user, uriInfo)){}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createUser(@Context HttpServletRequest httpRequest, final UserDto user) {
        try {
            ParseUtils.parseUser(user);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        final String locale = ApiUtils.getLocale(httpRequest);
        Optional<User> opNewUser;
        try {
            opNewUser = userService.adminCreate(user.getUsername(), user.getPassword(), user.getMail(), locale);
        } catch (DataIntegrityViolationException | UserException ex) {
            LOGGER.warn("User creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
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
    @Path("/recover/{userId}")
    public Response recoverUser(@PathParam("userId") long userId) {
        try{
            userService.recoverUser(userId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        LOGGER.debug("User {} recovered", userId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{userId}/edit/username")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateUsername(@PathParam("userId") long userId,
                                   final UserDto user) {
        if (user == null) return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();

        try {
            ParseUtils.parseUsername(user.getUsername());
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<User> opUser;
        try {
            opUser = userService.updateUsername(userId, user.getUsername());
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, "Username already taken.");
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
    public Response updatePassword(@PathParam("userId") long userId,
                                   final PasswordDto dto) {
        try {
            ParseUtils.parsePassword(dto.getOldPassword());
            ParseUtils.parsePassword(dto.getNewPassword());
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<User> opUser;
        try {
            opUser = userService.updatePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        } catch(InvalidPasswordException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
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
