package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Request;
import ar.edu.itba.paw.models.constants.RequestStatus;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class RequestDto {

    private Long id;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private RequestStatus status;

    private URI user;
    private URI target;
    private URI pet;

    private Long userId;
    private Long petId;

    public static RequestDto fromRequest(Request request, UriInfo uriInfo) {
        final RequestDto dto = new RequestDto();

        dto.id = request.getId();
        dto.creationDate = request.getCreationDate();
        dto.updateDate = request.getUpdateDate();
        dto.status = request.getStatus();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(request.getUser().getId())).build();
        dto.target = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(request.getTarget().getId())).build();
        dto.pet = uriInfo.getBaseUriBuilder().path("pets").path(String.valueOf(request.getPet().getId())).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
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

    public URI getPet() {
        return pet;
    }

    public void setPet(URI pet) {
        this.pet = pet;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }
}
