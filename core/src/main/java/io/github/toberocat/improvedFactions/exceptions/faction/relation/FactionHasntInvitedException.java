package io.github.toberocat.improvedFactions.exceptions.faction.relation;

import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class FactionHasntInvitedException extends Exception {
    public FactionHasntInvitedException(@NotNull Faction<?> inviter, @NotNull Faction<?> invited) {
        super("Faction " + invited + " hasn't been invited by " + inviter);
    }
}
