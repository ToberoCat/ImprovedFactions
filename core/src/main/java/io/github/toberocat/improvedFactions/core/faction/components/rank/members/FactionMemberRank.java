package io.github.toberocat.improvedFactions.core.faction.components.rank.members;

public class FactionMemberRank extends FactionRank {
    public static final String REGISTRY = "Member";
    public static final String KEY = REGISTRY.toLowerCase();

    public FactionMemberRank(int priority) {
        super(KEY, REGISTRY, priority, false,
                "fe8fc22eb8a994f669fb64ff8c5bb153874f471a159c34f8916c7adea998ff");
    }
}
