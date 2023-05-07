package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.exceptions.TranslatableRuntimeException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface FactionPlayer extends CommandSender, OfflineFactionPlayer {

    @Nullable String getMessage(@NotNull String query, @NotNull Map<String, Function<Translatable, String>> placeholders);

    @Nullable String[] getMessages(@NotNull String query, @NotNull Map<String, Function<Translatable, String>> placeholders);

    @NotNull Location getLocation();

    @NotNull String getLocal();

    @NotNull ItemStack getMainItem();

    void sendTitle(@NotNull String title,
                   @NotNull String subtitle);

    void sendTitle(@NotNull String titleQuery,
                   @NotNull String subtitleQuery,
                   @NotNull Map<String, Function<Translatable, String>> placeholders);

    void sendActionBar(@NotNull String actionbarQuery, @NotNull Map<String, Function<Translatable, String>> placeholders);

    void closeGuis();
}
