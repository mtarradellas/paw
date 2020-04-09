package ar.edu.itba.paw.models;

public class User {
    private long id;
    private String username;
    private String password;
    private String mail;
    private String phone;

    public User() {

    }

    public User(long id, String username, String password, String mail, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
    }

    public User(long id, String username, String mail, String phone) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.phone = phone;
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