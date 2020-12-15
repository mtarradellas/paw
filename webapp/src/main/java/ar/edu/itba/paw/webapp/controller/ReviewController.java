package ar.edu.itba.paw.webapp.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.ReviewService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.ReviewException;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;

@Component
@Path("/reviews")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private static final int REV_PAGE_SIZE = 12;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReviewList(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("userId") @DefaultValue("0") long userId,
                                  @QueryParam("targetId") @DefaultValue("0") long targetId,
                                  @QueryParam("minScore") @DefaultValue("1") int minScore,
                                  @QueryParam("maxScore") @DefaultValue("5") int maxScore,
                                  @QueryParam("status") @DefaultValue("-1") int status,
                                  @QueryParam("searchCriteria") String searchCriteria,
                                  @QueryParam("searchOrder") String searchOrder) {

        Long user;
        Long target;
        ReviewStatus reviewStatus;
        try {
            ParseUtils.parsePage(page);
            user = ParseUtils.parseUserId(userId);
            target = ParseUtils.parseUserId(targetId);
            ParseUtils.parseReviewScore(minScore, maxScore);
            reviewStatus = ParseUtils.parseStatus(ReviewStatus.class, status);
            searchCriteria = ParseUtils.parseCriteria(searchCriteria);
            searchOrder = ParseUtils.parseOrder(searchOrder);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        List<ReviewDto> reviewList;
        int amount;
        double average;
        try {
            reviewList = reviewService.reviewList(user, target, minScore, maxScore, reviewStatus,
                    searchCriteria, searchOrder, page, REV_PAGE_SIZE)
                    .stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());
            amount = reviewService.getReviewListAmount(user, target, minScore, maxScore, reviewStatus);
            average = reviewService.getReviewAverage(user, target, minScore, maxScore, reviewStatus);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Map<String, Object> json = new HashMap<>();
        json.put("amount", amount);
        json.put("average", average);
        json.put("pagesize", REV_PAGE_SIZE);
        json.put("pages", (int) Math.ceil((double) amount / (double) REV_PAGE_SIZE));
        json.put("reviewList", reviewList);

        return ApiUtils.paginatedListResponse(amount, REV_PAGE_SIZE, page, uriInfo, new Gson().toJson(json));
    }

    @GET
    @Path("/{reviewId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReview(@PathParam("reviewId") long reviewId) {
        final Optional<Review> opReview = reviewService.findReviewById(reviewId);

        if (!opReview.isPresent()) {
            LOGGER.warn("Review {} not found.", reviewId);
            final ErrorDto body = new ErrorDto(1, "Review not found.");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        final Review review = opReview.get();
        return Response.ok(new GenericEntity<ReviewDto>(ReviewDto.fromReview(review, uriInfo)){}).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response createReview(final ReviewDto reviewDto) {
        try {
            ParseUtils.parseReview(reviewDto);
        } catch (BadRequestException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(userService, auth);
        if (currentUser == null || currentUser.getId() != reviewDto.getUserId()) {
            LOGGER.warn("User has no permission to perform this action.");
            final ErrorDto body = new ErrorDto(2, "User has no permissions to perform this action.");
            return Response.status(Response.Status.FORBIDDEN.getStatusCode())
                                    .entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Optional<Review> opReview;
        try {
            opReview = reviewService.addReview(reviewDto.getUserId(), reviewDto.getTargetId(), reviewDto.getScore(), reviewDto.getDescription());
        } catch (DataIntegrityViolationException | NotFoundException | ReviewException ex) {
            LOGGER.warn("Review creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(3, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        if (!opReview.isPresent()) {
            LOGGER.warn("Review creation failed, service returned empty review");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
        final Review review = opReview.get();

        final URI reviewUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(review.getId())).build();
        return Response.created(reviewUri).build();
    }

    @DELETE
    @Path("/{reviewId}")
    public Response deleteReview(@PathParam("reviewId") long reviewId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(userService, auth);
        
        try {
            reviewService.removeReview(currentUser.getId(), reviewId);
        } catch (NotFoundException | ReviewException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        LOGGER.debug("Review {} removed", reviewId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{reviewId}/edit/score")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateScore(@PathParam("reviewId") long reviewId,
                                final ReviewDto dto) {
        try {
            ParseUtils.parseReviewScore(dto.getScore());
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(userService, auth);

        Optional<Review> opReview;
        try {
            opReview = reviewService.updateReviewScore(currentUser.getId(), reviewId, dto.getScore());
        } catch (NotFoundException | ReviewException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(2, ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        if (!opReview.isPresent()) {
            LOGGER.warn("Review score update failed. Service returned empty review");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/{reviewId}/edit/description")
    @Consumes(value = { MediaType.APPLICATION_JSON})
    public Response updateDescription(@PathParam("reviewId") long reviewId,
                                      final ReviewDto dto) {
        try {
            ParseUtils.parseReviewDescription(dto.getDescription());
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ApiUtils.loggedUser(userService, auth);

        Optional<Review> opReview;
        try {
            opReview = reviewService.updateReviewDescription(currentUser.getId(), reviewId, dto.getDescription());
        } catch (NotFoundException | ReviewException ex) {
            LOGGER.warn(ex.getMessage());
            final ErrorDto body = new ErrorDto(1, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(new GenericEntity<ErrorDto>(body){}).build();
        }
        if (!opReview.isPresent()) {
            LOGGER.warn("Review description update failed. Service returned empty review");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }
}
