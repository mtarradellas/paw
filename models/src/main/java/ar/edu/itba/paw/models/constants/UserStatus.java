package ar.edu.itba.paw.models.constants;

public enum UserStatus {
    ACTIVE(0),
    INACTIVE(1),
    DELETED(2);

    final private int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
