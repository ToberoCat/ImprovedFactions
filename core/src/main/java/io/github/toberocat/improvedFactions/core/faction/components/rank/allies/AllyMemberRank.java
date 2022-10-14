package io.github.toberocat.improvedFactions.core.faction.components.rank.allies;

import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionMemberRank;

public class AllyMemberRank extends AllyRank {

    public static final String registry = AllyRank.ALLY_IDENTIFIER + FactionMemberRank.REGISTRY;

    public AllyMemberRank() {
        super(FactionMemberRank.KEY, registry, "95492eb79fe0c306761fb7cfc3dfb054c0b7740adf8650ac5182062a05a783f1");
    }
}
