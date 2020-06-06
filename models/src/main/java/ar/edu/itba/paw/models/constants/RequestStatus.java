package ar.edu.itba.paw.models.constants;

public enum RequestStatus {
    PENDING(0),
    ACCEPTED(1),
    REJECTED(2),
    CANCELED(3);

    final private int value;

    RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
