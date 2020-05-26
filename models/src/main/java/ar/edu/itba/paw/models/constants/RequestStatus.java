package ar.edu.itba.paw.models.constants;

public enum RequestStatus {
    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    CANCELED(4);

    private int value;

    RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
