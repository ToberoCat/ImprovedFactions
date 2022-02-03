package io.github.toberocat.core.utility;

/**
 * This class should be used to return multiply setds of data
 * @param <T> First object (type as T)
 * @param <E> Second object (type as E)
 */
public class ObjectPair<T, E> {
    private T first;
    private E second;

    /**
     * Create your object with the predefined types
     * @param first type class for t
     * @param second type class for e
     */
    public ObjectPair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public synchronized void setT(T first) {
        this.first = first;
    }

    public synchronized void setE(T first) {
        this.first = first;
    }

    /**
     * Get the first type
     * @return The t type set
     */
    public T getT() {
        return first;
    }

    /**
     * Get the second type
     * @return The e type set
     */
    public E getE() {
        return second;
    }
}
