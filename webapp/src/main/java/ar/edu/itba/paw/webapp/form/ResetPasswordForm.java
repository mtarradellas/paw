package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.customValidators.FieldsValueMatch;

import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords do not match!"
        )
})
public class ResetPasswordForm {
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
}
