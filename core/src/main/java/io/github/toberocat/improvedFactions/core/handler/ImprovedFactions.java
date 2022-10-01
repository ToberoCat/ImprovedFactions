package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.handler.component.PlayerLister;
import io.github.toberocat.improvedFactions.core.handler.component.Scheduler;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface ImprovedFactions<RawWorld> {

    static @NotNull ImprovedFactions<?> api() {
        ImprovedFactions<?> implementation = ImplementationHolder.improvedFactions;
        if (implementation == null) throw new NoImplementationProvidedException("improved faction");
        return implementation;
    }

    /* Player */
    @Nullable FactionPlayer<?> getPlayer(@NotNull UUID id);

    @Nullable FactionPlayer<?> getPlayer(@NotNull String name);

    @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull UUID id);

    @Nullable OfflineFactionPlayer<?> getOfflinePlayer(@NotNull String name);

    @NotNull PlayerLister<?, ?> listPlayers();

    void broadcast(@NotNull String message);

    void runCommand(@NotNull String command);

    /* Scheduler */

    @NotNull Scheduler getScheduler();

    /* World */

    @Nullable World<RawWorld> getWorld(@NotNull String name);

    @NotNull List<World<RawWorld>> getAllWorlds();

    /* File */

    @NotNull File getLocalDataFolder();
    @NotNull File getDataFolder();
    @NotNull File getTempFolder();
    @NotNull File getLangFolder();
    @NotNull File getGuiFolder();

    /* Config */

    @NotNull ConfigHandler getConfig(@NotNull String relativePath);

}
