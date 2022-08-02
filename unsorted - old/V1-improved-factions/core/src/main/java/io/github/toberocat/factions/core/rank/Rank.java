package io.github.toberocat.factions.core.rank;

import io.github.toberocat.factions.core.rank.allies.*;
import io.github.toberocat.factions.core.rank.members.*;
import io.github.toberocat.factions.core.wrapper.inventory.ItemWrapper;
import io.github.toberocat.factions.core.wrapper.player.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

public abstract class Rank {
    private static final LinkedHashMap<String, Rank> RANKS = new LinkedHashMap<>();
    protected final String registryName;
    protected final boolean isAdmin;
    protected final int priority;

    protected String displayName;

    public Rank(String displayName, String registryName, int permissionPriority, boolean isAdmin) {
        this.displayName = displayName;
        this.registryName = registryName;
        this.isAdmin = isAdmin;
        this.priority = permissionPriority;
        RANKS.put(registryName, this);
    }

    public static Stream<Rank> getPriorityRanks(@NotNull Rank rank) {
        int priority = getPriority(rank);
        return RANKS.values().stream().filter(x -> x.priority >= 0 && x.priority < priority);
    }

    public static Rank fromString(@NotNull String str) {
        return RANKS.get(str);
    }

    public static int getPriority(Rank rank) {
        return rank.priority < 0 ?
                rank.getRegistryName().equals(OwnerRank.registry)
                        ? 100
                        : -1
                : rank.priority;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRegistryName() {
        return registryName;
    }

    public abstract ItemWrapper icon(PlayerWrapper player);

    public abstract String description(PlayerWrapper player);

    /**
     * @return raw priority. negative numbers can get returned,
     * as they mean that it is the highest priority and not selectable
     */
    public int getRawPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return registryName;
    }
}
