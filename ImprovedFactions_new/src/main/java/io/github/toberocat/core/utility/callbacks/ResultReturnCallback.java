package io.github.toberocat.core.utility.callbacks;

public interface ResultReturnCallback<T, R> {
    R call(T t);
}
