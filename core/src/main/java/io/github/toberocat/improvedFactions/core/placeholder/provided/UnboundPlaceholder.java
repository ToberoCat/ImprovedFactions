package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;;

public interface UnboundPlaceholder {
    boolean canParse(@NotNull String placeholder);

    @NotNull String apply(@NotNull OfflineFactionPlayer player, @NotNull String placeholder);
}
