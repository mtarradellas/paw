package ar.edu.itba.paw.models;

import java.util.Date;
import java.util.UUID;

public class Token {
    private long id;
    private UUID token;
    private long userId;
    private Date expirationDate;

    public Token(long id, UUID token, long userId, Date expirationDate) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
