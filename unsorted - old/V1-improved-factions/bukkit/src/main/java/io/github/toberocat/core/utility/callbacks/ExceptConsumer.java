package io.github.toberocat.core.utility.callbacks;

import java.util.function.Consumer;

public interface ExceptConsumer<T> extends Consumer<T> {

    void run(T t) throws Exception;

    @Override
    default void accept(T t) {
        try {
            run(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
