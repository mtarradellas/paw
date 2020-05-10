package ar.edu.itba.paw.models;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    DELETED;

    public int getValue() {
        return ordinal() + 1;
    }
}

