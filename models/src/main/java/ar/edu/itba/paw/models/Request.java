package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.RequestStatus;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

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
    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    @IndexedEmbedded(depth = 1)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "petId")
    private Pet pet;


    protected Request() {
        // Hibernate
    }

    public Request(Date creationDate, RequestStatus status, Pet pet) {
        this.creationDate = creationDate;
        this.pet = pet;
        this.status = status;
    }

    public Request(Date creationDate, RequestStatus status, User user, Pet pet) {
        this.creationDate = creationDate;
        this.pet = pet;
        this.status = status;
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

    public long getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
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
