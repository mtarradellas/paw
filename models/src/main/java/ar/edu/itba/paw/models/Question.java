package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.QuestionStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "questions_id_seq", name = "questions_id_seq")
    private Long id;

    @Column
    private String content;

    @OneToOne(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Answer answer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerid")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "targetid")
    private User target;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "petid")
    private Pet pet;

    @Column
    private LocalDateTime creationDate;

    @Column
    private int status;

    protected Question() {
        // Hibernate
    }

    public Question(String content, User user, User target, Pet pet, LocalDateTime creationDate, QuestionStatus status) {
        this.content = content;
        this.user = user;
        this.target = target;
        this.pet = pet;
        this.creationDate = creationDate;
        this.status = status.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", content: " + content + ", user: " + user.getId() + ", target: " + target.getId() +
                ", pet: " + pet.getId() + ", creationDate: " + creationDate + " }";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public QuestionStatus getStatus() {
        return QuestionStatus.values()[status];
    }

    public void setStatus(QuestionStatus status) {
        this.status = status.ordinal();
    }

    public Map<String, Object> toCommentJson() {
        Map<String, Object> question = new HashMap<>();
        question.put("id", id);
        question.put("content", content);
        question.put("ownerUsername", user.getUsername());
        question.put("ownerId", user.getId());
        question.put("targetUsername", target.getUsername());
        question.put("targetId", target.getId());
        question.put("creationDate", creationDate);
        return question;
    }
}
