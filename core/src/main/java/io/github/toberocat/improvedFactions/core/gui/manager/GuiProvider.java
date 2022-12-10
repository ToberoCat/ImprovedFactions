package io.github.toberocat.improvedFactions.core.gui.manager;

import io.github.toberocat.improvedFactions.core.gui.content.Flag;
import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public interface GuiProvider {
    @NotNull String getGuiId();
    @NotNull String[] getStates();
    @NotNull Flag[] getFlags();
    @NotNull ItemState getState(@NotNull FactionPlayer<?> viewer,
                                @NotNull Map<String, ItemState> states);
    @NotNull ItemStack createFromState(@NotNull FactionPlayer<?> viewer,
                                       @NotNull ItemState state);
}
