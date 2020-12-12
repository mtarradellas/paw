package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.interfaces.exceptions.NotFoundException;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.constants.ReviewStatus;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import ar.edu.itba.paw.webapp.util.ParseUtils;
import com.google.gson.Gson;
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
@Path("/reviews")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private static final int REV_PAGE_SIZE = 12;

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
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        List<ReviewDto> reviewList;
        int amount;
        try {
            reviewList = userService.reviewList(user, target, minScore, maxScore, reviewStatus,
                    searchCriteria, searchOrder, page, REV_PAGE_SIZE)
                    .stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());
            amount = userService.getReviewListAmount(user, targetId, minScore, maxScore, reviewStatus);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return ApiUtils.paginatedListResponse(amount, REV_PAGE_SIZE, page, uriInfo, new GenericEntity<List<ReviewDto>>(reviewList) {});
    }

    @GET
    @Path("/{reviewId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReview(@PathParam("reviewId") long reviewId) {
        final Optional<Review> opReview = userService.findReviewById(reviewId);

        if (!opReview.isPresent()) {
            LOGGER.warn("Review {} not found", reviewId);
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
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
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Optional<Review> opReview;
        try {
            opReview = userService.addReview(reviewDto.getUserId(), reviewDto.getTargetId(), reviewDto.getScore(), reviewDto.getDescription());
        } catch (DataIntegrityViolationException | NotFoundException ex) {
            LOGGER.warn("Review creation failed with exception");
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
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
        try {
            userService.removeReview(reviewId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        LOGGER.debug("Review {} removed", reviewId);
        return Response.noContent().build();
    }

    @GET
    @Path("/info")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReviewsInfo(@QueryParam("userId") @DefaultValue("0") long userId,
                                   @QueryParam("targetId") @DefaultValue("0") long targetId,
                                   @QueryParam("minScore") @DefaultValue("1") int minScore,
                                   @QueryParam("maxScore") @DefaultValue("5") int maxScore,
                                   @QueryParam("status") @DefaultValue("-1") int status) {

        ReviewStatus reviewStatus;
        Long user;
        Long target;
        try {
            user = ParseUtils.parseUserId(userId);
            target = ParseUtils.parseUserId(targetId);
            ParseUtils.parseReviewScore(minScore, maxScore);
            reviewStatus = ParseUtils.parseStatus(ReviewStatus.class, status);
        } catch (BadRequestException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        int amount;
        double average;
        try {
            amount = userService.getReviewListAmount(user, target, minScore, maxScore, reviewStatus);
            average = userService.getReviewAverage(user, target, minScore, maxScore, reviewStatus);
        } catch (NotFoundException ex) {
            LOGGER.warn("{}", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Map<String, Object> json = new HashMap<>();
        json.put("amount", amount);
        json.put("average", average);

        return Response.ok().entity(new Gson().toJson(json)).build();
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
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Optional<Review> opReview;
        try {
            opReview = userService.updateReviewScore(reviewId, dto.getScore());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
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
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Optional<Review> opReview;
        try {
            opReview = userService.updateReviewDescription(reviewId, dto.getDescription());
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        if (!opReview.isPresent()) {
            LOGGER.warn("Review description update failed. Service returned empty review");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("{reviewId}/recover")
    public Response recoverReview(@PathParam("reviewId") long reviewId) {
        try {
            userService.recoverReview(reviewId);
        } catch (NotFoundException ex) {
            LOGGER.warn(ex.getMessage());
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
        LOGGER.debug("Review {} recovered", reviewId);
        return Response.noContent().build();
    }
}
