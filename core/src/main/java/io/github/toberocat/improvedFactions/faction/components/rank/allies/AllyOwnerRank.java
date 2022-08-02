package io.github.toberocat.improvedFactions.faction.components.rank.allies;

import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionOwnerRank;

public class AllyOwnerRank extends AllyRank {

    public static final String registry = ALLY_IDENTIFIER + FactionOwnerRank.REGISTRY;

    public AllyOwnerRank() {
        super(FactionOwnerRank.KEY, registry, "134b0ab8933d5366d7c4c1dc0b0ccc97dcd1d20a4baaa8b04598a4bfaf26b59");
    }
}
