package ar.edu.itba.paw.models;

public class User {
    private long id;
    private String username;
    private String password;
    private String location;
    private String mail;
    private String phone;
    private String sex;
    private String birthDate;

    public User() {

    }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(long id, String username, String password, String location, String mail, String phone, String sex, String birthDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.location = location;
        this.mail = mail;
        this.phone = phone;
        this.sex = sex;
        this.birthDate = birthDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}