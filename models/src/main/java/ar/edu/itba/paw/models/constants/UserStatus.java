package ar.edu.itba.paw.models.constants;

public enum UserStatus {
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3);

    private int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
