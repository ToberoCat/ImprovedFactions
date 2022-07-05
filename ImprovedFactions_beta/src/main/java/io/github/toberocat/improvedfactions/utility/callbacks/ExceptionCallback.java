package io.github.toberocat.improvedfactions.utility.callbacks;

import io.github.toberocat.improvedfactions.utility.Utils;

public interface ExceptionCallback extends Callback {
    default void callback() {
        try {
            ECallback();
        } catch (Exception e) {
            Utils.except(e);
        }
    }
    void ECallback() throws Exception;
}
