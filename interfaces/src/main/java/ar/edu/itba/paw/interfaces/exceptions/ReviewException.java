package ar.edu.itba.paw.interfaces.exceptions;

public class ReviewException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReviewException() {
    }

    public ReviewException(String s) {
        super(s);
    }

    public ReviewException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ReviewException(Throwable throwable) {
        super(throwable);
    }

    public ReviewException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
