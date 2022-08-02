package io.github.toberocat.improvedFactions.faction.components.rank.members;

public class FactionElderRank extends FactionRank {

    public static final String REGISTRY = "Elder";
    public static final String KEY = REGISTRY.toLowerCase();

    public FactionElderRank(int priority) {
        super(KEY, REGISTRY, priority, false,
                "845a1ff7aefa1fdc8db690620d02fc6005fdbe81b823e1b7fee9115de073b435");
    }
}
