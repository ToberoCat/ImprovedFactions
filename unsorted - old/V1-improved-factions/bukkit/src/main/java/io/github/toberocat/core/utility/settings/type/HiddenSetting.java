package io.github.toberocat.core.utility.settings.type;

import io.github.toberocat.core.utility.callbacks.ResultReturnCallback;
import io.github.toberocat.core.utility.callbacks.ReturnCallback;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HiddenSetting<T> extends Setting<T> {

    private final ResultReturnCallback<String, T> from;

    @Override
    public void fromString(@NotNull String value) {
        selected = from.call(value);
    }

    @Override
    public String toString() {
        return selected.toString();
    }

    public HiddenSetting(String settingName, ResultReturnCallback<String, T> from, T t) {
        super(settingName, t, null);
        this.from = from;
    }
}
