package ar.edu.itba.paw.models.constants;

import java.util.HashMap;
import java.util.Map;

public enum PriceRange {
    CERO(0, -1),
    ONE(0, 0),
    TWO(1, 4999),
    THREE(5000, 9999),
    FOUR(10000, 14999),
    FIVE(15000, 19999),
    SIX(20000, 24999),
    SEVEN(25000, -1);

    private final int min;
    private final int max;

    PriceRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int min() {
        return this.min;
    }

    public int max() {
        return this.max;
    }

    public Map<String, Integer> asMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("min", min());
        map.put("max", max());
        return map;
    }
}