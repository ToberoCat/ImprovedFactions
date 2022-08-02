package io.github.toberocat.improvedFactions.faction.components.rank.members;

public class FactionOwnerRank extends FactionRank {
    public static final String REGISTRY = "Owner";
    public static final String KEY = REGISTRY.toLowerCase();

    public FactionOwnerRank(int priority) {
        super(KEY, REGISTRY, priority, true,
                "e081e9b6034f715fd02f5b4d311a489f8f6aca72724d85bfd19649655d5cb257");
    }
}
