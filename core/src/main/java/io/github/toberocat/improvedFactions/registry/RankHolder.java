package io.github.toberocat.improvedFactions.registry;

import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import org.jetbrains.annotations.NotNull;

public interface RankHolder {
    @NotNull Rank getOwner();
    @NotNull Rank getAllyOwner();
    @NotNull Rank getAdmin();
    @NotNull Rank getAllyAdmin();
    @NotNull Rank getModerator();
    @NotNull Rank getAllyModerator();
}
