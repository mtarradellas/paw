package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.UserStatus;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name = "users_id_seq")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String mail;

    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    @OneToMany(orphanRemoval = true, mappedBy = "ownerid", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column
    private List<Request> requestList;

    protected User() {
        // Hibernate
    }

    public User(String username, String password, String mail, UserStatus status) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.status = status;
    }

    @Deprecated
    public User(String username, String mail) {
        this.username = username;
        this.mail = mail;
    }

    @Deprecated
    public User(long id, String username, String mail) {
        this.id = id;
        this.username = username;
        this.mail = mail;
    }

    @Deprecated
    public User(long id, String username, String password, String mail, UserStatus status) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id.intValue();
        hash = 31 * hash + username.hashCode();
        return hash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

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

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

}