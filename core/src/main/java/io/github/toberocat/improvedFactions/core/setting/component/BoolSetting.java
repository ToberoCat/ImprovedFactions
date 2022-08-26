package io.github.toberocat.improvedFactions.core.setting.component;

import io.github.toberocat.improvedFactions.core.setting.Setting;
import org.jetbrains.annotations.NotNull;

public class BoolSetting extends Setting<Boolean> {
    private final @NotNull String label;

    public BoolSetting(@NotNull String label) {
        this.label = label;
    }

    @Override
    public @NotNull Boolean createFromSave(@NotNull String saved) {
        return saved.equals("true");
    }

    @Override
    public @NotNull String toSave(Boolean value) {
        return String.valueOf(value);
    }

    public @NotNull String label() {
        return label;
    }
}
