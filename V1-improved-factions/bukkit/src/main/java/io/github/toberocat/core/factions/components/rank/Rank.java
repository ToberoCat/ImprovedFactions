package io.github.toberocat.core.factions.components.rank;

import io.github.toberocat.core.factions.components.rank.allies.*;
import io.github.toberocat.core.factions.components.rank.members.*;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

public abstract class Rank {

    public static LinkedHashMap<String, Rank> ranks = new LinkedHashMap<>();
    private final String registryName;
    private final String displayName;
    private final boolean isAdmin;
    private final int priority;

    public Rank(String displayName, String registryName, int permissionPriority, boolean isAdmin) {
        this.displayName = displayName;
        this.registryName = registryName;
        this.isAdmin = isAdmin;
        this.priority = permissionPriority;
        ranks.put(registryName, this);
    }
    
    public abstract @NotNull Rank getEquivalent();

    public static void register() {
        new OwnerRank(-1);
        new AdminRank(3);
        new ModeratorRank(2);
        new ElderRank(1);
        new MemberRank(0);

        new AllyOwnerRank(-1);
        new AllyAdminRank(-1);
        new AllyModeratorRank(-1);
        new AllyElderRank(-1);
        new AllyMemberRank(-1);

        new GuestRank(-1);
    }
    
    public static Stream<Rank> getPriorityRanks(Rank rank) {
        int priority = getPriority(rank);
        return ranks.values().stream().filter(x -> x.priority >= 0 && x.priority < priority);
    }

    public static @NotNull Rank fromString(String str) {
        return ranks.getOrDefault(str, ranks.get(GuestRank.register));
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

    public String getRegistryName() {
        return registryName;
    }

    public abstract String description(Player player);

    public ItemStack getItem(Player player) {
        return Utility.createItem(Material.GRASS_BLOCK, getDisplayName());
    }

    /**
     *
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
