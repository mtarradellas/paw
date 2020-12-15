package ar.edu.itba.paw.webapp.dto;

import java.net.URI;
import java.time.LocalDateTime;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.models.Review;

public class ReviewDto {

    private Long id;
    private Integer score;
    private String description;
    private LocalDateTime creationDate;
    private int status;

    private URI user;
    private URI target;

    private Long userId;
    private Long targetId;
    private String username;

    public static ReviewDto fromReview(Review review, UriInfo uriInfo) {
        final ReviewDto dto = new ReviewDto();

        dto.id = review.getId();
        dto.score = review.getScore();
        dto.description = review.getDescription();
        dto.status = review.getStatus().getValue();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getOwner().getId())).build();
        dto.target = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(review.getTarget().getId())).build();
        dto.username = review.getOwner().getUsername();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
