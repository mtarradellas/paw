package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Review;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class ReviewDto {

    private Long id;
    private int score;
    private String description;
    private LocalDateTime creationDate;
    private URI user;
    private URI target;

    public static ReviewDto fromReview(Review review, UriInfo uriInfo) {
        final ReviewDto dto = new ReviewDto();

        dto.id = review.getId();
        dto.score = review.getScore();
        dto.description = review.getDescription();
//        dto.user = uriInfo.getAbsolutePathBuilder()
//        dto.target = uriInfo.getAbsolutePathBuilder()

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getTarget() {
        return target;
    }

    public void setTarget(URI target) {
        this.target = target;
    }
}
