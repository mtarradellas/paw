package ar.edu.itba.paw.models.constants;

public enum PetStatus {
    AVAILABLE(1),
    REMOVED(2),
    SOLD(3),
    UNAVAILABLE(4);

    final private int value;

    PetStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
