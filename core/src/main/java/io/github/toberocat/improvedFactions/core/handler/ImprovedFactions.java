package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.gui.GuiApi;
import io.github.toberocat.improvedFactions.core.handler.component.PlayerLister;
import io.github.toberocat.improvedFactions.core.handler.component.Scheduler;
import io.github.toberocat.improvedFactions.core.player.ConsoleCommandSender;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface ImprovedFactions {

    static @NotNull ImprovedFactions api() {
        ImprovedFactions implementation = ImplementationHolder.improvedFactions;
        if (implementation == null) throw new NoImplementationProvidedException("improved faction");
        return implementation;
    }

    /* Player */
    @Nullable FactionPlayer getPlayer(@NotNull UUID id);

    @Nullable FactionPlayer getPlayer(@NotNull String name);

    @Nullable OfflineFactionPlayer getOfflinePlayer(@NotNull UUID id);

    @Nullable OfflineFactionPlayer getOfflinePlayer(@NotNull String name);

    @NotNull PlayerLister<?, ?> listPlayers();

    void broadcast(@NotNull String message);

    void runCommand(@NotNull String command);

    /* Scheduler */

    @NotNull Scheduler getScheduler();

    /* World */

    @Nullable World getWorld(@NotNull String name);

    @NotNull List<World> getAllWorlds();

    /* File */

    @NotNull File getLocalDataFolder();
    @NotNull File getDataFolder();
    @NotNull File getTempFolder();
    @NotNull File getLangFolder();
    @NotNull File getGuiFolder();

    /* Config */

    @NotNull ConfigFile getConfig(@NotNull String relativePath);
    @NotNull ConfigFile getConfig();

    /* Sender */

    /**
     * Gives an instance of the console sender
     * @return The console sender for this server
     */
    @NotNull ConsoleCommandSender getConsoleSender();

    @NotNull GuiApi getGuis();

}
