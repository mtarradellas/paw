package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.ReviewStatus;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;

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
    private Date creationDate;

    protected Review() {
        // Hibernate
    }

    public Review(User owner, User target, int score, String description, ReviewStatus status, Date uploadDate) {
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

    public Date getCreationDate() {
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

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
