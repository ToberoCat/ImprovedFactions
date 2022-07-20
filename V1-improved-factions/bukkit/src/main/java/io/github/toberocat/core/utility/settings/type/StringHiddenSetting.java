package io.github.toberocat.core.utility.settings.type;

import org.jetbrains.annotations.NotNull;

public class StringHiddenSetting extends Setting<String> {
    @Override
    public void fromString(@NotNull String value) {
        selected = value;
    }

    @Override
    public String toString() {
        return selected;
    }

    public StringHiddenSetting(String settingName, String value) {
        super(settingName, value, null);
    }
}
