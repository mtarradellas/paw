package ar.edu.itba.paw.webapp.exception;

public class PetNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7711783757526047305L;

    public PetNotFoundException() {
        super();
    }

    public PetNotFoundException(String message) {
        super(message);
    }

    public PetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PetNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
