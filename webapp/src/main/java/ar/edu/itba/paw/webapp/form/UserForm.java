package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.FieldsValueMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "repeatPassword"
        )
})
public class UserForm {
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @Size(min = 4, max = 50)
    private String password;

    @Size(min = 4, max = 50)
    private String repeatPassword;

    @NotBlank
    @Email
    private String mail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword)
    {
        this.repeatPassword = repeatPassword;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}