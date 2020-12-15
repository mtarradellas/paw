package ar.edu.itba.paw.webapp.controller.admin;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
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
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.RequestService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.RequestException;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.constants.RequestStatus;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.RequestDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;

@Component
@Path("/admin/requests")
public class AdminRequestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRequestController.class);

    @Autowired
    private RequestService requestService;

    private static final int REQ_PAGE_SIZE = 25;

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
            user = ParseUtils.parseUserId(userId);
            target = ParseUtils.parseUserId(targetId);
            pet = ParseUtils.parsePetId(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
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
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return ApiUtils.paginatedListResponse(amount, REQ_PAGE_SIZE, page, uriInfo, requestList, null);
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
       
        if (requestDto == null || requestDto.getPetId() == null || requestDto.getUserId() == null) {
            final ErrorDto body = new ErrorDto(1, "Missing required request fields.");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<Request> opRequest;
        try {
            opRequest = requestService.create(locale, requestDto.getId(), requestDto.getPetId(), uriInfo.getBaseUri().toString());
        } catch (DataIntegrityViolationException | NotFoundException | RequestException ex) {
            LOGGER.warn("Request creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        if (!opRequest.isPresent()) {
            LOGGER.warn("Request creation failed, service returned empty request");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final Request request = opRequest.get();

        final URI requestUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(request.getId())).build();
        return Response.created(requestUri).build();
    }

    @GET
    @Path("/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRequestFilters(@QueryParam("userId") @DefaultValue("0") long userId,
                                      @QueryParam("petId") @DefaultValue("0") long petId,
                                      @QueryParam("status") @DefaultValue("-1") int status) {

        RequestStatus requestStatus;
        Long user;
        Long pet;
        try {
            user = ParseUtils.parseUserId(userId);
            pet = ParseUtils.parsePetId(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

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
                final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
            }
        }
        List<Integer> statusIdList = statusList.stream().map(RequestStatus::getValue).collect(Collectors.toList());

        Map<String, Object> filters = new TreeMap<>();
        filters.put("statusList", statusIdList);

        return Response.ok().entity(new Gson().toJson(filters)).build();   
    }

    @GET
    @Path("/interests/filters")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getInterestFilters(@QueryParam("targetId") @DefaultValue("0") long targetId,
                                      @QueryParam("petId") @DefaultValue("0") long petId,
                                      @QueryParam("status") @DefaultValue("-1") int status) {

        RequestStatus requestStatus;
        Long target;
        Long pet;
        try {
            target = ParseUtils.parseUserId(targetId);
            pet = ParseUtils.parsePetId(petId);
            requestStatus = ParseUtils.parseStatus(RequestStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        /** Status Filter */
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
                final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
            }
        }
        List<Integer> statusIdList = statusList.stream().map(RequestStatus::getValue).collect(Collectors.toList());

        /** Pet Filter */
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
                final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
            }
        }

        Map<String, Object> filters = new TreeMap<>();
        filters.put("statusList", statusIdList);
        filters.put("petList", petList);

        return Response.ok().entity(new Gson().toJson(filters)).build();
    }

    @POST
    @Path("/{requestId}/edit")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response editRequest(final RequestDto requestDto) {
        if (requestDto == null || requestDto.getId() == null || requestDto.getStatus() == null) {
            final ErrorDto body = new ErrorDto(1, "Missing required fields.");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        RequestStatus status;
        try {
            status = ParseUtils.parseStatus(RequestStatus.class, requestDto.getStatus());
        } catch (BadRequestException ex) {
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        try {
            requestService.adminUpdateStatus(requestDto.getId(), status);
        } catch (NotFoundException | RequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        return Response.noContent().build();
    }
}
