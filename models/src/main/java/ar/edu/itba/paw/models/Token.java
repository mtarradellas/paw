package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "tokens_id_seq", name = "tokens_id_seq")
    private Long id;

    @Column
    private UUID token;

    @Column
    private Date expirationDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    protected Token() {
        // Hibernate
    }

    public Token(UUID token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public Token(UUID token, Date expirationDate, User user) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
