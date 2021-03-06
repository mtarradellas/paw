package ar.edu.itba.paw.interfaces.exceptions;

public class PetException extends RuntimeException{
    private static final long serialVersionUID = -235865344109373586L;

    public PetException() {
    }

    public PetException(String s) {
        super(s);
    }

    public PetException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PetException(Throwable throwable) {
        super(throwable);
    }

    public PetException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
