package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Indexed
@Table(name = "Requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "requests_id_seq", name = "requests_id_seq")
    @DocumentId
    private Long id;

    @Column
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime updateDate;

    @Field
    @NumericField
    private int status;

    @IndexedEmbedded(depth = 3)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "targetid")
    private User target;

    @IndexedEmbedded(depth = 3)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "petId")
    private Pet pet;


    protected Request() {
        // Hibernate
    }

    public Request(LocalDateTime creationDate, RequestStatus status, User user, User target, Pet pet) {
        this.creationDate = creationDate;
        this.pet = pet;
        this.status = status.getValue();
        this.user = user;
        this.target = target;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", status: " + status + ", owner: " + user + ", pet: " + pet + ", creationDate: " + creationDate + " }";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return RequestStatus.values()[status];
    }

    public void setStatus(RequestStatus status) {
        this.status = status.getValue();
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
