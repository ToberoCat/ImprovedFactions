package io.github.toberocat.improvedFactions.faction.components.rank.allies;


import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionAdminRank;

public class AllyAdminRank extends AllyRank {

    public static final String registry = ALLY_IDENTIFIER + FactionAdminRank.REGISTRY;

    public AllyAdminRank() {
        super(FactionAdminRank.KEY, registry, "cb5b0c77a2d41d8d7144e8a56bb9c456ea9812d1ce665c7867fae849e8c7c931");
    }

}
