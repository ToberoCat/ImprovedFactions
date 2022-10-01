package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface FactionPlayer<P> extends CommandSender, OfflineFactionPlayer<P> {

    @Nullable String getMessage(@NotNull Function<Translatable, String> query, Placeholder... placeholders);

    @Nullable String[] getMessageBatch(@NotNull Function<Translatable, String[]> query, Placeholder... placeholders);

    @NotNull Location getLocation();

    @NotNull String getLocal();

    @NotNull ItemStack getMainItem();

    void sendTitle(@NotNull String title, @NotNull String subtitle);

    void sendTitle(@NotNull Function<Translatable, String> title);

    void sendActionBar(@NotNull String actionbar);

    void sendActionBar(@NotNull Function<Translatable, String> actionbar);

    void closeGuis();
}
