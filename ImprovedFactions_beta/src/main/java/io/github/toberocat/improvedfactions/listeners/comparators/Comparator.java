package io.github.toberocat.improvedfactions.listeners.comparators;

public interface Comparator<T> {
    boolean Compare(T type);

    public static void ComapareAll(Object compare, Comparator[] comparators) {
        for (Comparator comparator : comparators) {
            comparator.Compare(compare);
        }
    }
}
