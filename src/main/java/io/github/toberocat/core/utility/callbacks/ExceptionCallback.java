package io.github.toberocat.core.utility.callbacks;

import io.github.toberocat.core.utility.Utility;

public interface ExceptionCallback extends Callback {
    default void callback() {
        try {
            ECallback();
        } catch (Exception e) {
            Utility.except(e);
        }
    }
    void ECallback() throws Exception;
}
