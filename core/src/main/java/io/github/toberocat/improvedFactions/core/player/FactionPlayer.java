package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.location.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface FactionPlayer extends CommandSender, OfflineFactionPlayer {

    @Nullable String getMessage(@NotNull String query, @NotNull Map<String, String> placeholders);

    @Nullable String[] getMessages(String s, HashMap<String, String> placeholders);


    @NotNull Location getLocation();

    @NotNull String getLocal();

    @NotNull ItemStack getMainItem();

    void sendTitle(@NotNull String title,
                   @NotNull String subtitle);
    void sendTitle(@NotNull String titleQuery,
                   @NotNull String subtitleQuery,
                   @NotNull Map<String, String> placeholders);

    void sendActionBar(@NotNull String actionbarQuery, @NotNull Map<String, String> placeholders);

    void closeGuis();
}
