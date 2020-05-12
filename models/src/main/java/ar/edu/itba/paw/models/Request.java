package ar.edu.itba.paw.models;

import java.util.Date;

public class Request {
    private long id;
    private long ownerId;
    private String ownerUsername;
    private Status status;
    private long petId;
    private String petName;
    private Date creationDate;

    public Request(long id,long ownerId, String ownerUsername, Status status, long petId, String petName, Date creationDate) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.status = status;
        this.petId = petId;
        this.petName = petName;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", status: " + status + ", owner: " + ownerId + ", pet: " + petId + ", creationDate: " + creationDate + " }";
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
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
