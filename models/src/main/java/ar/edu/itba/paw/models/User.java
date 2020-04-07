package ar.edu.itba.paw.models;

public class User {
    private String id;
    private String name;
    private String password;
    private String location;
    private String mail;
    private String phone;
    private String sex;
    private String birthDate;

    public User() {

    }

    public User(String id, String name, String password, String location, String mail, String phone, String sex, String birthDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.location = location;
        this.mail = mail;
        this.phone = phone;
        this.sex = sex;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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