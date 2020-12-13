package ar.edu.itba.paw.webapp.exception;

public class UserNotRequestOwnerException extends RuntimeException{
    private static final long serialVersionUID = -8622151943732570228L;

    public UserNotRequestOwnerException() {
        super();
    }

    public UserNotRequestOwnerException(String message) {
        super(message);
    }

    public UserNotRequestOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotRequestOwnerException(Throwable cause) {
        super(cause);
    }

    protected UserNotRequestOwnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
