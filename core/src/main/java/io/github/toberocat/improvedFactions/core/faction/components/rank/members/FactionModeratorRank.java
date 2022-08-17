package io.github.toberocat.improvedFactions.core.faction.components.rank.members;

public class FactionModeratorRank extends FactionRank {
    public static final String REGISTRY = "Moderator";
    public static final String KEY = REGISTRY.toLowerCase();

    public FactionModeratorRank(int priority) {
        super(KEY, REGISTRY, priority, false,
                "7c56bfd76e7b4bce7216d3489a4635e11c345de852faa0932159b4afde18b25c");
    }
}
