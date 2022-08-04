package io.github.toberocat.improvedFactions.exceptions.faction.relation;

import io.github.toberocat.improvedFactions.faction.Faction;
import org.jetbrains.annotations.NotNull;

public class AlreadyInvitedException extends Exception {
    public AlreadyInvitedException(@NotNull Faction<?> invited) {
        super("Faction " + invited + "already got invited");
    }
}
