package io.github.toberocat.improvedFactions.faction.components.rank.members;

import io.github.toberocat.core.factions.components.rank.Rank;
import org.jetbrains.annotations.NotNull;

import static io.github.toberocat.MainIF.config;

public abstract class FactionRank extends Rank {

    public FactionRank(@NotNull String displayPath,
                       @NotNull String registry,
                       int permissionPriority,
                       boolean isAdmin) {
        super(config().getString(displayPath, registry), registry, permissionPriority, isAdmin);
    }

    @Override
    public final @NotNull Rank getEquivalent() {
        return Rank.fromString("Ally" + getRegistryName());
    }

}
