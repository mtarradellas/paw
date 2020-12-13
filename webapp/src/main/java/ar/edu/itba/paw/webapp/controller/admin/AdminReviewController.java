// package ar.edu.itba.paw.webapp.controller.admin;

// public class AdminReviewController {
//     @POST
//     @Path("{reviewId}/recover")
//     public Response recoverReview(@PathParam("reviewId") long reviewId) {
//         try {
//             reviewService.recoverReview(reviewId);
//         } catch (NotFoundException ex) {
//             LOGGER.warn(ex.getMessage());
//             return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//         }
//         LOGGER.debug("Review {} recovered", reviewId);
//         return Response.noContent().build();
//     }
// }
