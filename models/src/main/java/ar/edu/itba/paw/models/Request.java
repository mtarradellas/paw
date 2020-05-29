package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.RequestStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "requests_id_seq", name = "requests_id_seq")
    private Long id;

    @Column
    private Date creationDate;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private User user;

    @Column
    private Long petId;

    protected Request() {
        // Hibernate
    }

    public Request(Date creationDate, RequestStatus status, Long petId) {
        this.creationDate = creationDate;
        this.petId = petId;
        this.status = status;
    }

    public Request(Date creationDate, RequestStatus status, User user, Long petId) {
        this.creationDate = creationDate;
        this.petId = petId;
        this.status = status;
        this.user = user;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", status: " + status + ", owner: " + user + ", pet: " + petId + ", creationDate: " + creationDate + " }";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPetName() {
        return "misterius pet";
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

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
