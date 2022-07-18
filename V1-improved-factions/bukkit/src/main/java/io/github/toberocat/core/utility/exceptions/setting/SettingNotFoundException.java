package io.github.toberocat.core.utility.exceptions.setting;

import org.jetbrains.annotations.NotNull;

public class SettingNotFoundException extends Exception {
    public SettingNotFoundException(@NotNull String setting) {
        super("Wasn't able to find requested setting " + setting);
    }
}
