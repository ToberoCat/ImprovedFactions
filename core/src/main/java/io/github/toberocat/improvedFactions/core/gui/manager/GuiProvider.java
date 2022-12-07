package io.github.toberocat.improvedFactions.core.gui.manager;

import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface GuiProvider {
    @NotNull String getGuiId();
    @NotNull String[] getStates();

    @NotNull ItemState getState(@NotNull Map<String, ItemState> states);
}
