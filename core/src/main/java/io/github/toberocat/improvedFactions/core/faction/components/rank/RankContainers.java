package io.github.toberocat.improvedFactions.core.faction.components.rank;

import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionAdminRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionElderRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionMemberRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionModeratorRank;

import java.util.List;

public class RankContainers {
    public static final List<String> DEFAULT_FACTION_RANKS_CONTAINERS = List.of(
            FactionMemberRank.REGISTRY,
            FactionElderRank.REGISTRY,
            FactionModeratorRank.REGISTRY,
            FactionAdminRank.REGISTRY
    );
}
