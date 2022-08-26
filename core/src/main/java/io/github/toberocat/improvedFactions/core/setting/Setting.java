package io.github.toberocat.improvedFactions.core.setting;

import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class Setting<T> {

    private static final Map<String, Setting<?>> SETTING_MAP = new HashMap<>();

    public Setting() {
        SETTING_MAP.put(label(), this);
    }

    public static @Nullable Setting<?> getSetting(@NotNull String label) {
        return SETTING_MAP.get(label);
    }

    @NotNull
    public abstract T createFromSave(@NotNull String saved) throws ErrorParsingSettingException;

    @NotNull
    public abstract String toSave(T value);

    @NotNull
    public abstract String label();
}
