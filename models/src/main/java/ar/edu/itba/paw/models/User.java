package ar.edu.itba.paw.models;

public class User {
    private long id;
    private String username;
    private String password;
    private String mail;
    private String phone;
    private Status status;

    public User(String username, String mail, String phone) {
        this.username = username;
        this.mail = mail;
        this.phone = phone;
    }

    public User(long id, String username, String mail, String phone) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.phone = phone;
    }

    public User(String username, String password, String mail, String phone) {
        this.username = username;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
    }

    public User(long id, String username, String password, String mail, String phone, Status status) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}