package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.groups.BasicInfoEditUser;
import ar.edu.itba.paw.webapp.form.groups.ChangePasswordEditUser;
import ar.edu.itba.paw.webapp.validators.FieldsValueDifferent;
import ar.edu.itba.paw.webapp.validators.FieldsValueMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "newPassword",
                fieldMatch = "repeatNewPassword",
                groups = ChangePasswordEditUser.class
        )
})
@FieldsValueDifferent.List({
        @FieldsValueDifferent(
                field = "currentPassword",
                otherField = "newPassword",
                groups = ChangePasswordEditUser.class
        )
})
public class EditUserForm {

    @Size(min = 4, max = 50, groups = ChangePasswordEditUser.class)
    private String currentPassword;

    @Size(min = 4, max = 50, groups = ChangePasswordEditUser.class)
    private String newPassword;

    private String repeatNewPassword;

    @Size(min = 4, max = 50, groups = BasicInfoEditUser.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = BasicInfoEditUser.class)
    private String username;

    @Pattern(regexp = "^[0-9]+$", groups = BasicInfoEditUser.class)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
}