package ar.edu.itba.paw.webapp.form;

public class QuestionAnswerForm {
    private String content;

    public QuestionAnswerForm(){

    }

    public QuestionAnswerForm(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
