package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.webapp.dto.RequestDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("/requests")
public class RequestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    private static final int REQ_PAGE_SIZE = 25;

    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRequestList(@QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("userId") @DefaultValue("0") long userId,
                                   @QueryParam("targetId") @DefaultValue("0") long targetId,
                                   @QueryParam("petId") @DefaultValue("0") long petId,
                                   @QueryParam("status") @DefaultValue("-1") int status,
                                   @QueryParam("searchCriteria") String searchCriteria,
                                   @QueryParam("searchOrder") String searchOrder) {

        RequestStatus requestStatus;
        Long user;
        Long target;
        Long pet;
        try {
            ParseUtils.parsePage(page);
            user = ParseUtils.parseUser(userId);
            target = ParseUtils.parseUser(targetId);
            pet = ParseUtils.parsePet(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        List<RequestDto> requestList;
        int amount;
        try {
            requestList = requestService.filteredList(user, target, pet, Collections.emptyList(), requestStatus,
                    searchCriteria, searchOrder, page, REQ_PAGE_SIZE)
                    .stream().map(r -> RequestDto.fromRequest(r, uriInfo)).collect(Collectors.toList());
            amount = requestService.getFilteredListAmount(user, target, pet, Collections.emptyList(), requestStatus);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return ApiUtils.paginatedListResponse(amount, REQ_PAGE_SIZE, page, uriInfo, new GenericEntity<List<RequestDto>>(requestList) {});
    }

    @GET
    @Path("/{requestId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRequest(@PathParam("requestId") long requestId) {
        final Optional<Request> opRequest = requestService.findById(requestId);

        if (!opRequest.isPresent()) {
            LOGGER.warn("Request {} not found", requestId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        final Request request = opRequest.get();
        return Response.ok(new GenericEntity<RequestDto>(RequestDto.fromRequest(request, uriInfo)){}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createRequest(final RequestDto requestDto) {
        final String locale = ApiUtils.getLocale();
        Optional<Request> opRequest;
        try {
             opRequest = requestService.create(locale, requestDto.getUserId(), requestDto.getPetId(), uriInfo.getBaseUri().getPath());
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("Request creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        if (!opRequest.isPresent()) {
            LOGGER.warn("Request creation failed, service returned empty request");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final Request request = opRequest.get();

        final URI userUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(request.getId())).build();
        return Response.created(userUri).build();
    }

    @GET
    @Path("/amount")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUserRequestsAmount(@QueryParam("userId") @DefaultValue("0") long userId,
                                          @QueryParam("targetId") @DefaultValue("0") long targetId,
                                          @QueryParam("petId") @DefaultValue("0") long petId,
                                          @QueryParam("status") @DefaultValue("-1") int status) {

        RequestStatus requestStatus;
        Long user;
        Long target;
        Long pet;
        try {
            user = ParseUtils.parseUser(userId);
            target = ParseUtils.parseUser(targetId);
            pet = ParseUtils.parseUser(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        int amount;
        try {
            amount = requestService.getFilteredListAmount(user, target, pet, Collections.emptyList(), requestStatus);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Map<String, Integer> json = new HashMap<>();
        json.put("amount", amount);

        return Response.ok().entity(new Gson().toJson(json)).build();
    }

    @GET
    @Path("/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRequestFilters(@QueryParam("userId") @DefaultValue("0") long userId,
                                      @QueryParam("targetId") @DefaultValue("0") long targetId,
                                      @QueryParam("petId") @DefaultValue("0") long petId,
                                      @QueryParam("status") @DefaultValue("-1") int status) {

        RequestStatus requestStatus;
        Long user;
        Long target;
        Long pet;
        try {
            user = ParseUtils.parseUser(userId);
            target = ParseUtils.parseUser(targetId);
            pet = ParseUtils.parsePet(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        /* TODO join request and interest methods into 1 service method for both */
        // Requests
        if (user != null) {
            List<RequestStatus> statusList;
            if(requestStatus != null)  {
                statusList = new ArrayList<>();
                statusList.add(requestStatus);
            }
            else {
                try {
                    statusList = requestService.filteredStatusList(user, pet, Collections.emptyList(), null);
                } catch (NotFoundException ex) {
                    LOGGER.warn("{}", ex.getMessage());
                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
                }
            }
            List<Integer> statusIdList = statusList.stream().map(RequestStatus::getValue).collect(Collectors.toList());

            Map<String, Object> filters = new TreeMap<>();
            filters.put("statusList", statusIdList);

            return Response.ok().entity(new Gson().toJson(filters)).build();
        }

        // Interests
        if (target != null) {
            List<RequestStatus> statusList;
            if(requestStatus != null) {
                statusList = new ArrayList<>();
                statusList.add(requestStatus);
            }
            else {
                try {
                    statusList = requestService.filteredStatusListByPetOwner(target, pet, Collections.emptyList(), null);
                } catch (NotFoundException ex) {
                    LOGGER.warn("{}", ex.getMessage());
                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
                }
            }
            List<Integer> statusIdList = statusList.stream().map(RequestStatus::getValue).collect(Collectors.toList());

            /* TODO return petDTO instead of just ids?*/
            List<Long> petList;
            if (pet != null) {
                petList = new ArrayList<>();
                petList.add(pet);
            }
            else {
                try {
                    petList = requestService.filteredPetListByPetOwner(target, pet, Collections.emptyList(), requestStatus)
                            .stream().map(Pet::getId).collect(Collectors.toList());
                } catch (NotFoundException ex) {
                    LOGGER.warn("{}", ex.getMessage());
                    return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
                }
            }

            Map<String, Object> filters = new TreeMap<>();
            filters.put("statusList", statusIdList);
            filters.put("petList", petList);

            return Response.ok().entity(new Gson().toJson(filters)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
    }
}
