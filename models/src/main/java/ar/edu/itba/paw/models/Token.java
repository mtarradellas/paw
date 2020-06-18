package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "tokens_id_seq", name = "tokens_id_seq")
    private Long id;

    @Column
    private UUID token;

    @Column
    private LocalDateTime expirationDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    protected Token() {
        // Hibernate
    }

    public Token(UUID token, LocalDateTime expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public Token(UUID token, LocalDateTime expirationDate, User user) {
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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
