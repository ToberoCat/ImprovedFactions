package io.github.toberocat.improvedFactions.core.handler.component;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface PlayerLister<On, Off> {
    @NotNull Stream<UUID> getPlayers();
    @NotNull Stream<UUID> getOnlinePlayers();

    @NotNull Stream<String> getPlayerNames();
    @NotNull Stream<String> getOnlinePlayerNames();

    @NotNull List<Off> getRawOfflinePlayers();
    @NotNull List<On> getRawOnlinePlayers();
}
