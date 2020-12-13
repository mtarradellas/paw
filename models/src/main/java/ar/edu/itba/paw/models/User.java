package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.LongBridge;

import ar.edu.itba.paw.models.constants.UserStatus;

@Entity
@Table(name = "Users")
@Indexed
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name = "users_id_seq")
    @Field(name= "eid")
    @FieldBridge(impl = LongBridge.class)
    @SortableField
    private Long id;

    @Column(nullable = false)
    @Field
    @SortableField
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Field
    @SortableField
    private String mail;

    @Column(length = 7)
    private String locale;

    @Field(store = Store.YES)
    @NumericField
    private int status;

    @Column
    private LocalDateTime interestsDate;

    @Column
    private LocalDateTime requestsDate;

    @ContainedIn
    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column
    private List<Request> requestList;

    @OneToMany(orphanRemoval = true, mappedBy = "target", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column
    private List<Request> interestList;

    @IndexedEmbedded(depth = 1)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column
    private List<Pet> petList;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @Column
    private List<Review> ownerReviews;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    @Column
    private List<Review> targetReviews;

    @OneToMany(mappedBy = "newOwner", fetch = FetchType.LAZY)
    @Column
    private List<Pet> newPets;

    protected User() {
        // Hibernate
    }

    public User(String username, String password, String mail, UserStatus status, String locale) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.status = status.getValue();
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
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

    @Override
    public String toString() {
        return "{ id: " + id + ", username: " + username + ", mail: " + mail + ", status: " + status + " }";
    }

    public UserStatus getStatus() {
        return UserStatus.values()[status];
    }

    public void setStatus(UserStatus status) {
        this.status = status.getValue();
    }

    public Long getId() {
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

    public List<Request> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Request> interestList) {
        this.interestList = interestList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<Review> getOwnerReviews() {
        return ownerReviews;
    }

    public void setOwnerReviews(List<Review> ownerReviews) {
        this.ownerReviews = ownerReviews;
    }

    public List<Review> getTargetReviews() {
        return targetReviews;
    }

    public void setTargetReviews(List<Review> targetReviews) {
        this.targetReviews = targetReviews;
    }

    public double getAverageScore() {
        int amount = targetReviews.size();
        if (amount == 0) return -1;
        int sum = targetReviews.stream().mapToInt(Review::getScore).sum();
        return (double) sum / amount;
    }

    public List<Pet> getNewPets() {
        return newPets;
    }

    public void setNewPets(List<Pet> newPets) {
        this.newPets = newPets;
    }

    public LocalDateTime getInterestsDate() {
        return interestsDate;
    }

    public void setInterestsDate(LocalDateTime interestsDate) {
        this.interestsDate = interestsDate;
    }

    public LocalDateTime getRequestsDate() {
        return requestsDate;
    }

    public void setRequestsDate(LocalDateTime requestsDate) {
        this.requestsDate = requestsDate;
    }
}