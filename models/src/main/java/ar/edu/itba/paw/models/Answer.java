package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.constants.QuestionStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answers_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "answers_id_seq", name = "answers_id_seq")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "questionId", referencedColumnName = "id")
    private Question question;

    @Column
    private String content;

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

    protected Answer() {
        // Hibernate
    }

    public Answer(Question question, String content, User user, User target, Pet pet, LocalDateTime creationDate, QuestionStatus status) {
        this.question = question;
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
        Answer answer = (Answer) o;
        return id.equals(answer.id);
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", question: " + question.getId() + ", content: " + content + ", user: " + user.getId() + ", target: " + target.getId() +
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> toCommentJson() {
        Map<String, Object> answer = new HashMap<>();
        answer.put("content", content);
        answer.put("ownerUsername", user.getUsername());
        answer.put("ownerId", user.getId());
        answer.put("targetUsername", target.getUsername());
        answer.put("targetId", target.getId());
        answer.put("creationDate", creationDate);
        return answer;
    }
}
