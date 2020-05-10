package ar.edu.itba.paw.webapp.exception;

public class ImageLoadException extends RuntimeException{
    public ImageLoadException() {
        super();
    }

    public ImageLoadException(String message) {
        super(message);
    }

    public ImageLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageLoadException(Throwable cause) {
        super(cause);
    }

    protected ImageLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
