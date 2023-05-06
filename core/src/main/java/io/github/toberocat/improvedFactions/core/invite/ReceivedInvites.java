package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public record ReceivedInvites(@NotNull OfflineFactionPlayer received,
                              @NotNull OfflineFactionPlayer sender,
                              @NotNull Faction<?> faction,
                              @NotNull FactionRank rank) {
}
