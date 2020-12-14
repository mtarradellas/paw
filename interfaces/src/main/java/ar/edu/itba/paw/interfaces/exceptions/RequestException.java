package ar.edu.itba.paw.interfaces.exceptions;

public class RequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RequestException() {
    }

    public RequestException(String s) {
        super(s);
    }

    public RequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RequestException(Throwable throwable) {
        super(throwable);
    }

    public RequestException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
