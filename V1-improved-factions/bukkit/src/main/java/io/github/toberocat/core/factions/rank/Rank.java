package io.github.toberocat.core.factions.rank;

import io.github.toberocat.core.factions.rank.allies.*;
import io.github.toberocat.core.factions.rank.members.*;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Rank {

    public static List<Rank> ranks = new ArrayList<>();
    private final String registryName;
    private final String displayName;
    private final boolean isAdmin;
    private final int priority;

    public Rank(String displayName, String registryName, int permissionPriority, boolean isAdmin) {
        this.displayName = displayName;
        this.registryName = registryName;
        this.isAdmin = isAdmin;
        this.priority = permissionPriority;
        ranks.add(this);
    }

    public static void Init() {
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
        return ranks.stream().filter(x -> x.priority >= 0 && x.priority < priority);
    }

    public static Rank fromString(String str) {
        for (Rank rank : ranks) {
            if (rank.toString().equals(str)) return rank;
        }
        return null;
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
