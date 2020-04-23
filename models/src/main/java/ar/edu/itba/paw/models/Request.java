package ar.edu.itba.paw.models;

import java.util.Date;

public class Request {
    private long id;
    private String ownerUsername;
    private Status status;
    private long petId;
    private Date creationDate;

    public Request(long id, String ownerUsername, Status status, long petId, Date creationDate) {
        this.id = id;
        this.ownerUsername = ownerUsername;
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

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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
