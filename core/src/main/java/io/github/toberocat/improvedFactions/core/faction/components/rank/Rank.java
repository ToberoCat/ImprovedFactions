package io.github.toberocat.improvedFactions.core.faction.components.rank;

import io.github.toberocat.improvedFactions.core.faction.components.rank.allies.*;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.*;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Rank {

    public static final LinkedHashMap<String, Rank> ranks = new LinkedHashMap<>();
    protected final String registry;
    protected final Function<Translatable, String> title;
    private final boolean isAdmin;
    private final int priority;


    public Rank(Function<Translatable, String> title, String registryName, int permissionPriority, boolean isAdmin) {
        this.title = title;
        this.registry = registryName;
        this.isAdmin = isAdmin;
        this.priority = permissionPriority;
        ranks.put(registryName, this);
    }

    public static void register() {
        new FactionOwnerRank(-1);
        new FactionAdminRank(3);
        new FactionModeratorRank(2);
        new FactionElderRank(1);
        new FactionMemberRank(0);

        new AllyOwnerRank();
        new AllyAdminRank();
        new AllyModeratorRank();
        new AllyElderRank();
        new AllyMemberRank();

        new GuestRank();
    }

    public static Stream<Rank> getPriorityRanks(Rank rank) {
        int priority = getPriority(rank);
        return ranks.values().stream().filter(x -> x.priority >= 0 && x.priority < priority);
    }

    public static @NotNull Rank fromString(String str) {
        return ranks.getOrDefault(str, ranks.get(GuestRank.REGISTRY));
    }

    public static int getPriority(@NotNull Rank rank) {
        return rank.priority < 0 ?
                rank.getRegistry().equals(FactionOwnerRank.REGISTRY)
                        ? 100
                        : -1
                : rank.priority;
    }

    public abstract @NotNull Rank getEquivalent();

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getRegistry() {
        return registry;
    }

    public abstract @NotNull String[] description(FactionPlayer<?> player);

    public abstract @NotNull String title(FactionPlayer<?> player);

    public abstract ItemStack getItem(FactionPlayer<?> player);

    /**
     * @return raw priority. negative numbers can get returned,
     * as they mean that it is the highest priority and not selectable
     * or they just have no priority at all, depends on the given context
     */
    public int getRawPriority() {
        return priority;
    }

    public Function<Translatable, String> getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return registry;
    }
}
