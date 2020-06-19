package ar.edu.itba.paw.models.constants;

public enum RequestStatus {
    PENDING(0),
    ACCEPTED(1),
    REJECTED(2),
    CANCELED(3),
    SOLD(4);

    final private int value;

    RequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static int maxValue() {
        return SOLD.ordinal();
    }

    public static int amount() {
        return RequestStatus.values().length;
    }
}
