package io.github.toberocat.improvedFactions.faction.components.report;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface FactionReports {
    void addReport(@NotNull Player reporter, @NotNull String reason);

    @NotNull Stream<Report> getReports();
}
