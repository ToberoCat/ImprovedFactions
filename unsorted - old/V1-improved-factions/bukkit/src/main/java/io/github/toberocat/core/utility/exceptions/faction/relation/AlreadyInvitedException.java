package io.github.toberocat.core.utility.exceptions.faction.relation;

import io.github.toberocat.core.factions.Faction;
import org.jetbrains.annotations.NotNull;

public class AlreadyInvitedException extends Exception {
    public AlreadyInvitedException(@NotNull Faction<?> invited) {
        super("Faction " + invited + "already got invited");
    }
}
