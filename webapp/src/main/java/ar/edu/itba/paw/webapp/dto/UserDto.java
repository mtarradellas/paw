package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String mail;
    private URI requests;
    private URI interests;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.requests = uriInfo.getAbsolutePathBuilder().path("requests").build();
        dto.interests = uriInfo.getAbsolutePathBuilder().path("interests").build();

        // Not including password and mail for security

        return dto;
    }

    public static UserDto fromUserForList(User user, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.requests = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id)).path("requests").build();
        dto.interests = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id)).path("interests").build();

        // Not including password and mail for security

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getRequests() {
        return requests;
    }

    public void setRequests(URI requests) {
        this.requests = requests;
    }

    public URI getInterests() {
        return interests;
    }

    public void setInterests(URI interests) {
        this.interests = interests;
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
}
