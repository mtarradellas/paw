package ar.edu.itba.paw.interfaces.exception;

public class InvalidUserCreationException extends RuntimeException {

    private boolean duplicatedUsername;
    private boolean duplicatedMail;

    public InvalidUserCreationException() {
        super();
    }

    public InvalidUserCreationException(String message, boolean duplicatedUsername, boolean duplicatedMail) {
        super(message);
        this.duplicatedUsername = duplicatedUsername;
        this.duplicatedMail = duplicatedMail;
    }

    public InvalidUserCreationException(String message) {
        super(message);
    }

    public InvalidUserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserCreationException(Throwable cause) {
        super(cause);
    }

    protected InvalidUserCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isDuplicatedUsername() {
        return duplicatedUsername;
    }

    public boolean isDuplicatedMail() {
        return duplicatedMail;
    }
}
