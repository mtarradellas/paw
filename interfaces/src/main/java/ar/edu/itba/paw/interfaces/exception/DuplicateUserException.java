package ar.edu.itba.paw.interfaces.exception;

public class DuplicateUserException extends RuntimeException {

    private boolean duplicatedUsername;
    private boolean duplicatedMail;

    public DuplicateUserException() {
        super();
    }

    public DuplicateUserException(String message, boolean duplicatedUsername, boolean duplicatedMail) {
        super(message);
        this.duplicatedUsername = duplicatedUsername;
        this.duplicatedMail = duplicatedMail;
    }

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUserException(Throwable cause) {
        super(cause);
    }

    protected DuplicateUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isDuplicatedUsername() {
        return duplicatedUsername;
    }

    public boolean isDuplicatedMail() {
        return duplicatedMail;
    }
}
