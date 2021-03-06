package ar.edu.itba.paw.interfaces.exceptions;

public class InvalidImageQuantityException extends RuntimeException {
    private static final long serialVersionUID = 5443958084118708369L;

    public InvalidImageQuantityException() {
        super();
    }

    public InvalidImageQuantityException(String message) {
        super(message);
    }

    public InvalidImageQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageQuantityException(Throwable cause) {
        super(cause);
    }

    protected InvalidImageQuantityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
