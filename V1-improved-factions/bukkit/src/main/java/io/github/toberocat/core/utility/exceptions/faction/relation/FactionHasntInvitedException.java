package io.github.toberocat.core.utility.exceptions.faction.relation;

import io.github.toberocat.core.factions.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionHasntInvitedException extends Exception {
    public FactionHasntInvitedException(@NotNull Faction<?> inviter, @NotNull Faction<?> invited) {
        super("Faction " + invited + " hasn't been invited by " + inviter);
    }
}
