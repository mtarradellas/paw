package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Answer;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class AnswerDto {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private int status;
    private URI question;
    private URI user;
    private long userId;
    private URI target;
    private URI pet;

    public static AnswerDto fromAnswer(Answer answer, UriInfo uriInfo) {
        AnswerDto dto = new AnswerDto();

        dto.id = answer.getId();
        dto.content = answer.getContent();
        dto.creationDate = answer.getCreationDate();
        dto.status = answer.getStatus().getValue();
        dto.question = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(answer.getQuestion().getId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(answer.getUser().getId())).build();
        dto.target = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(answer.getTarget().getId())).build();
        dto.pet = uriInfo.getBaseUriBuilder().path("pets").path(String.valueOf(answer.getPet().getId())).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public URI getQuestion() {
        return question;
    }

    public void setQuestion(URI question) {
        this.question = question;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
