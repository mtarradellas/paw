package ar.edu.itba.paw.interfaces.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String s) {
        super(s);
    }

    public InvalidPasswordException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidPasswordException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPasswordException(String s, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(s, throwable, enableSuppression, writableStackTrace);
    }

}
