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

    public static RequestDto fromRequest(Request request) {
        final RequestDto dto = new RequestDto();

        dto.id = request.getId();
        dto.creationDate = request.getCreationDate();
        dto.updateDate = request.getUpdateDate();
        dto.status = request.getStatus();

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
}
