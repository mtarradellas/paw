package ar.edu.itba.paw.models.constants;

public enum ReviewStatus {
    VALID(1),
    REMOVED(2);

    final private int value;

    ReviewStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
