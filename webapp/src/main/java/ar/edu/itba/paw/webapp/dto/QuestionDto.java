package ar.edu.itba.paw.webapp.dto;

import java.net.URI;
import java.time.LocalDateTime;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.models.Question;

public class QuestionDto {

    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private int status;
    private Long userId;
    private Long petId;
    private URI target;
    private URI pet;
    private URI answer;
    private URI user;

    public static QuestionDto fromQuestion(Question question, UriInfo uriInfo) {
        QuestionDto dto = new QuestionDto();

        dto.id = question.getId();
        dto.content = question.getContent();
        dto.creationDate = question.getCreationDate();
        dto.status = question.getStatus().getValue();
        if(question.getAnswer() != null) dto.answer = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(question.getId())).path("answer").build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(question.getUser().getId())).build();
        dto.target = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(question.getTarget().getId())).build();
        dto.pet = uriInfo.getBaseUriBuilder().path("pets").path(String.valueOf(question.getPet().getId())).build();

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

    public URI getAnswer() {
        return answer;
    }

    public void setAnswer(URI answer) {
        this.answer = answer;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
