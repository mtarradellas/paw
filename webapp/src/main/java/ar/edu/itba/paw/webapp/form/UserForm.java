package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {

    @Size(min = 6, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @Size(min = 6, max = 50)
    private String password;

    @Email
    private String mail;

    @Pattern(regexp = "^[0-9]+$")
    private String phone;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
