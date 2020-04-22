package ar.edu.itba.paw.models;

import java.util.Date;

public class Request {
    private long id;
    private long ownerId;
    private Status status;
    private long petId;
    private Date creationDate;

    public Request(long id, long ownerId, Status status, long petId, Date creationDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.petId = petId;
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
