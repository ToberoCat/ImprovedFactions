package io.github.toberocat.improvedFactions.event;

import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.world.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * This class should emit events to make
 */
public interface EventListener {

    List<EventListener> HANDLER_LIST = new LinkedList<>();

    static void listen(@NotNull EventListener handler) {
        HANDLER_LIST.add(handler);
    }

    void protectChunk(@NotNull Chunk chunk, @NotNull String registry);

    void removeProtection(@NotNull Chunk chunk, @Nullable String oldRegistry);

    void factionMemberRankUpdate(@NotNull Faction<?> faction,
                                 @NotNull OfflineFactionPlayer<?> player,
                                 @NotNull FactionRank oldRank,
                                 @NotNull FactionRank newRank);

    void transferOwnership(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> oldOwner,
                           @NotNull FactionPlayer<?> newOwner);

    void joinMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank joinedAs);

    void leaveMember(@NotNull Faction<?> faction, @NotNull FactionPlayer<?> player);

    void kickMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player);

    void banMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player);

    void pardonPlayer(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player);

    void allyFaction(@NotNull Faction<?> first, @NotNull Faction<?> second);
}
