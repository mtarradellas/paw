package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class AdminUploadRequestForm {

    @NotNull
    private Long userId;

    @NotNull
    private Long petId;

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
