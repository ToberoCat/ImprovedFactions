package io.github.toberocat.improvedFactions.core.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class SettingGui implements GuiProvider {

    public static final String GUI_ID = "manage-faction";
    private final String[] states;

    public SettingGui() {
        this.states = new String[] {
                "defaultState"
        };
    }

    @Override
    public @NotNull String getGuiId() {
        return GUI_ID;
    }

    @Override
    public @NotNull String[] getStates() {
        return new String[0];
    }

    @Override
    public @NotNull ItemState getState(@NotNull Map<String, ItemState> states) {
        return null;
    }
}
