package io.github.toberocat.improvedFactions.faction.components.rank.members;

public class FactionAdminRank extends FactionRank {
    public static final String REGISTRY = "Admin";
    public static final String KEY = REGISTRY.toLowerCase();

    public FactionAdminRank(int priority) {
        super(KEY, REGISTRY, priority, true,
                "a890f37539e2d64fc9115ad05a4d2e9c76c10e5749ecdc2d4ad555fe46b63774");
    }
}
