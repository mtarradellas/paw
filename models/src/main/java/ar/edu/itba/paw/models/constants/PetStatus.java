package ar.edu.itba.paw.models.constants;

public enum PetStatus {
    AVAILABLE(0),
    REMOVED(1),
    SOLD(2),
    UNAVAILABLE(3);

    final private int value;

    PetStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
