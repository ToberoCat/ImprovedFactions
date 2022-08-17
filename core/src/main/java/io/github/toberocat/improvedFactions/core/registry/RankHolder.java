package io.github.toberocat.improvedFactions.core.registry;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import org.jetbrains.annotations.NotNull;

public interface RankHolder {

    static @NotNull RankHolder api() {
        RankHolder implementation = ImplementationHolder.rankHolder;
        if (implementation == null) throw new NoImplementationProvidedException("rank handler");
        return implementation;
    }

    @NotNull Rank getOwner();

    @NotNull Rank getAllyOwner();

    @NotNull Rank getAdmin();

    @NotNull Rank getAllyAdmin();

    @NotNull Rank getModerator();

    @NotNull Rank getAllyModerator();

    @NotNull Rank getElder();

    @NotNull Rank getAllyElder();

    @NotNull Rank getMember();

    @NotNull Rank getAllyMember();

    @NotNull Rank getGuest();
}
