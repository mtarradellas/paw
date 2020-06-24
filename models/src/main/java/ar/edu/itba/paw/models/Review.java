package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.ReviewStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "reviews_id_seq", name = "reviews_id_seq")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User owner;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "targetId")
    private User target;

    @Column
    private int score;

    @Column(length = 4086)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private ReviewStatus status;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    protected Review() {
        // Hibernate
    }

    public Review(User owner, User target, int score, String description, ReviewStatus status, LocalDateTime uploadDate) {
        this.owner = owner;
        this.target = target;
        this.score = score;
        this.description = description;
        this.status = status;
        this.creationDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public User getTarget() {
        return target;
    }

    public int getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> review = new HashMap<>();
        review.put("id", id);
        review.put("score", score);
        review.put("content", description);
        review.put("ownerUsername", owner.getUsername());
        review.put("ownerId", owner.getId());
        review.put("targetUsername", target.getUsername());
        review.put("targetId", target.getId());
        review.put("creationDate", creationDate);
        return review;
    }
}
