package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

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
    private Date creationDate;

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

    public Request(Date creationDate, RequestStatus status, Pet pet) {
        this.creationDate = creationDate;
        this.pet = pet;
        this.status = status.getValue();
    }

    public Request(Date creationDate, RequestStatus status, User user, Pet pet) {
        this.creationDate = creationDate;
        this.pet = pet;
        this.status = status.getValue();
        this.user = user;
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

    public long getId() {
        return id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
