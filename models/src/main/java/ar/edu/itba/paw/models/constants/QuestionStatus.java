package ar.edu.itba.paw.models.constants;

public enum QuestionStatus {
    VALID(0),
    REMOVED(1);

    final private int value;

    QuestionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
