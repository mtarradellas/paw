package ar.edu.itba.paw.models;

import java.util.List;

public class User {
    private long id;
    private String username;
    private String password;
    private String mail;
    private Status status;
    private List<Request> requestList;

    public User(String username, String mail) {
        this.username = username;
        this.mail = mail;
    }

    public User(long id, String username, String mail) {
        this.id = id;
        this.username = username;
        this.mail = mail;
    }

    public User(String username, String password, String mail) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public User(long id, String username, String password, String mail, Status status) {
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
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) id;
        hash = 31 * hash + username.hashCode();
        return hash;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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