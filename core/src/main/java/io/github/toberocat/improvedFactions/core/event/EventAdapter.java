package io.github.toberocat.improvedFactions.core.event;

import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.sender.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventAdapter implements EventListener {

    public EventAdapter() {
        EventListener.listen(this);
    }

    @Override
    public void protectChunk(@NotNull Chunk chunk, @NotNull String registry) {

    }

    @Override
    public void removeProtection(@NotNull Chunk chunk, @Nullable String oldRegistry) {

    }

    @Override
    public void factionMemberRankUpdate(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank oldRank, @NotNull FactionRank newRank) {

    }

    @Override
    public void transferOwnership(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> oldOwner, @NotNull FactionPlayer<?> newOwner) {

    }

    @Override
    public void joinMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player, @NotNull FactionRank joinedAs) {

    }

    @Override
    public void leaveMember(@NotNull Faction<?> faction, @NotNull FactionPlayer<?> player) {

    }

    @Override
    public void kickMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {

    }

    @Override
    public void banMember(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {

    }

    @Override
    public void pardonPlayer(@NotNull Faction<?> faction, @NotNull OfflineFactionPlayer<?> player) {

    }

    @Override
    public void allyFaction(@NotNull Faction<?> first, @NotNull Faction<?> second) {

    }
}
