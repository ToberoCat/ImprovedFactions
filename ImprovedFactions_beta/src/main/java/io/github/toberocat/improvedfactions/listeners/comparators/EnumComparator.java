package io.github.toberocat.improvedfactions.listeners.comparators;

public class EnumComparator<T extends Enum<T>> implements Comparator {

    private T aEnum;
    private ComparatorCallback callback;

    public EnumComparator(T aEnum, ComparatorCallback callback) {
        this.aEnum = aEnum;
        this.callback = callback;
    }

    @Override
    public boolean Compare(Object type) {
        if (aEnum == type) {
            callback.successfullCompared();
            return true;
        }
        return false;
    }
}
