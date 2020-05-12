package ar.edu.itba.paw.interfaces.exception;

public class InvalidImageQuantityException extends RuntimeException {

    public InvalidImageQuantityException() {
        super();
    }

    public InvalidImageQuantityException(String s) {
        super(s);
    }

    public InvalidImageQuantityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidImageQuantityException(Throwable throwable) {
        super(throwable);
    }

    public InvalidImageQuantityException(String s, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(s, throwable, enableSuppression, writableStackTrace);
    }
}
