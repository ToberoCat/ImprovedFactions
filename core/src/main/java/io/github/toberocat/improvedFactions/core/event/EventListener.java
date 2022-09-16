package io.github.toberocat.improvedFactions.core.event;

import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * This class should emit events to make
 *
 * Event calls could be sent from a thread, which threatens thread safety
 */
public interface EventListener {

    List<EventListener> HANDLER_LIST = new LinkedList<>();

    static void listen(@NotNull EventListener handler) {
        HANDLER_LIST.add(handler);
    }

    void protectChunk(@NotNull Chunk<?> chunk, @NotNull String registry);

    void removeProtection(@NotNull Chunk<?> chunk, @Nullable String oldRegistry);

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

    void createFaction(@NotNull Faction<?> faction, FactionPlayer<?> owner);

    void invitePlayer(@NotNull OfflineFactionPlayer<?> receiver,
                      @NotNull FactionPlayer<?> sender,
                      @NotNull Faction<?> faction,
                      @NotNull FactionRank rank);

    void cancelInvite(@NotNull OfflineFactionPlayer<?> receiver,
                @NotNull UUID sender,
                @NotNull Faction<?> faction,
                @NotNull String rank);

    void acceptInvite(@NotNull OfflineFactionPlayer<?> receiver,
                      @NotNull UUID sender,
                      @NotNull Faction<?> faction,
                      @NotNull FactionRank rank);
}
