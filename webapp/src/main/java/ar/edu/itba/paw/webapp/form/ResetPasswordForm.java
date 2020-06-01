package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.FieldsValueMatch;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords do not match!"
        )
})
public class ResetPasswordForm {
    @NotBlank
    private String token;

    @Size(min = 4, max = 50)
    private String password;

    @Size(min = 4, max = 50)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
