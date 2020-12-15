package ar.edu.itba.paw.interfaces.exceptions;

public class QuestionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QuestionException() {
    }

    public QuestionException(String s) {
        super(s);
    }

    public QuestionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public QuestionException(Throwable throwable) {
        super(throwable);
    }

    public QuestionException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
